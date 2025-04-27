import { Component, inject, OnInit } from '@angular/core';
import { DatePipe, NgClass } from '@angular/common';
import { IAppointment } from '../../appointment/appointment.model';
import { AppointmentService } from '../../appointment/service/appointment.service';
import SharedModule from '../../../shared/shared.module';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CanceledModalComponent } from './canceled-modal/canceled-modal.component';

@Component({
  templateUrl: 'appointment-service.component.html',
  standalone: true,
  styleUrl: 'appointment-service.component.scss',
  imports: [DatePipe, NgClass, SharedModule],
})
export class AppointmentServiceComponent implements OnInit {
  protected modalService = inject(NgbModal);
  appointments: any;
  constructor(private appointmentService: AppointmentService) {}
  ngOnInit() {
    this.load();
  }
  editAppointment(appointment: IAppointment) {
    // this.edit.emit(appointment);
  }
  load() {
    this.appointmentService.query({}).subscribe(data => {
      this.appointments = data.body;
    });
  }
  cancelAppointment(appointment: IAppointment) {
    const modalRef = this.modalService.open(CanceledModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.appointment = appointment;
    modalRef.closed.subscribe(() => {
      this.load();
    });
  }
}
