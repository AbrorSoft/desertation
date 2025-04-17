import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { DayOfWeek } from 'app/entities/enumerations/day-of-week.model';
import { WorkingHoursService } from '../service/working-hours.service';
import { IWorkingHours } from '../working-hours.model';
import { WorkingHoursFormService, WorkingHoursFormGroup } from './working-hours-form.service';

@Component({
  standalone: true,
  selector: 'jhi-working-hours-update',
  templateUrl: './working-hours-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WorkingHoursUpdateComponent implements OnInit {
  isSaving = false;
  workingHours: IWorkingHours | null = null;
  dayOfWeekValues = Object.keys(DayOfWeek);

  employeesSharedCollection: IEmployee[] = [];

  protected workingHoursService = inject(WorkingHoursService);
  protected workingHoursFormService = inject(WorkingHoursFormService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WorkingHoursFormGroup = this.workingHoursFormService.createWorkingHoursFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workingHours }) => {
      this.workingHours = workingHours;
      if (workingHours) {
        this.updateForm(workingHours);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workingHours = this.workingHoursFormService.getWorkingHours(this.editForm);
    if (workingHours.id !== null) {
      this.subscribeToSaveResponse(this.workingHoursService.update(workingHours));
    } else {
      this.subscribeToSaveResponse(this.workingHoursService.create(workingHours));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkingHours>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(workingHours: IWorkingHours): void {
    this.workingHours = workingHours;
    this.workingHoursFormService.resetForm(this.editForm, workingHours);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      workingHours.employee,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.workingHours?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
