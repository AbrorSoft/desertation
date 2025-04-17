import { Component, OnInit } from '@angular/core';
import { AppointmentServiceService } from './service/appointment-service.service';
import { DatePipe, NgClass } from '@angular/common';
import { IAppointment } from '../../appointment/appointment.model';

@Component({
  templateUrl: 'appointment-service.component.html',
  standalone: true,
  styleUrl: 'appointment-service.component.scss',
  imports: [DatePipe, NgClass],
})
export class AppointmentServiceComponent implements OnInit {
  appointments: any;
  constructor(private appointmentServiceService: AppointmentServiceService) {}
  ngOnInit() {
    this.appointmentServiceService.getAll().subscribe(data => {
      this.appointments = data;
    });
  }
  editAppointment(appointment: IAppointment) {
    // this.edit.emit(appointment);
  }

  cancelAppointment(appointment: IAppointment) {
    // this.cancel.emit(appointment);
  }
}
