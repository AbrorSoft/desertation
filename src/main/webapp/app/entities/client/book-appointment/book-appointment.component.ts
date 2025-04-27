import { Component, inject, OnInit } from '@angular/core';
import { BookAppointmentService } from './service/book-appointment.service';
import { DatePipe, NgClass, NgForOf, NgIf } from '@angular/common';
import { IAppointment } from '../../appointment/appointment.model';
import { AppointmentService } from '../../appointment/service/appointment.service';
import { RoomDeleteDialogComponent } from '../../room/delete/room-delete-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BookedModalComponent } from './booked-modal/booked-modal.component';
import TranslateDirective from '../../../shared/language/translate.directive';
import SharedModule from '../../../shared/shared.module';

@Component({
  templateUrl: 'book-appointment.component.html',
  standalone: true,
  styleUrl: 'book-appointment.component.scss',
  imports: [DatePipe, NgClass, NgIf, NgForOf, TranslateDirective, SharedModule],
})
export class BookAppointmentComponent implements OnInit {
  protected modalService = inject(NgbModal);
  appointments: any[] = [];
  selectedCategory: string = 'Gym'; // Default category

  constructor(private appointmentService: AppointmentService) {}
  ngOnInit() {
    this.appointmentService.query1({ 'status.equals:': 'GYM' }).subscribe(data => {
      this.filterAppointments(this.selectedCategory); // Initial query with default category
    });
  }
  filterAppointments(category: string) {
    this.selectedCategory = category; // Update the selected category
    console.log(category);
    this.appointmentService.query1({ 'serviceProviderType.equals': category.toUpperCase() }).subscribe(
      data => {
        // @ts-ignore
        this.appointments = data?.body.filter(data => !data?.user) || []; // Update appointments, fallback to empty array if null
      },
      error => {
        console.error('Error fetching appointments:', error);
        this.appointments = []; // Clear appointments on error
      },
    );
  }
  bookModalOpen(booked: any) {
    console.log(booked);
    const modalRef = this.modalService.open(BookedModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.booked = booked;
  }
}
