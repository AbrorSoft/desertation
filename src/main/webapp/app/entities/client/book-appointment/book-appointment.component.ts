import { Component, OnInit } from '@angular/core';
import { BookAppointmentService } from './service/book-appointment.service';
import { DatePipe, NgClass, NgForOf, NgIf } from '@angular/common';
import { IAppointment } from '../../appointment/appointment.model';
import { AppointmentService } from '../../appointment/service/appointment.service';

@Component({
  templateUrl: 'book-appointment.component.html',
  standalone: true,
  styleUrl: 'book-appointment.component.scss',
  imports: [DatePipe, NgClass, NgIf, NgForOf],
})
export class BookAppointmentComponent implements OnInit {
  appointments: any[] = [];
  selectedCategory: string = ''; // Default category

  constructor(private appointmentService: AppointmentService) {}
  ngOnInit() {
    this.appointmentService.query({ 'status.equals:': 'GYM' }).subscribe(data => {
      this.filterAppointments(this.selectedCategory); // Initial query with default category
    });
  }
  filterAppointments(category: string) {
    this.selectedCategory = category; // Update the selected category
    console.log(category);
    this.appointmentService.query({ 'serviceProvider.type.equals': category.toUpperCase() }).subscribe(
      data => {
        this.appointments = data.body || []; // Update appointments, fallback to empty array if null
      },
      error => {
        console.error('Error fetching appointments:', error);
        this.appointments = []; // Clear appointments on error
      },
    );
  }
}
