import { Component, OnInit } from '@angular/core';
import { AppointmentServiceService } from './service/appointment-service.service';

@Component({
  templateUrl: 'appointment-service.component.html',
  standalone: true,
})
export class AppointmentServiceComponent implements OnInit {
  appointment: any
  constructor(private appointmentServiceService: AppointmentServiceService) {}
  ngOnInit() {
      this.appointmentServiceService.getAll().subscribe(data=>{
        this.appointment  = data
      })
  }
}
