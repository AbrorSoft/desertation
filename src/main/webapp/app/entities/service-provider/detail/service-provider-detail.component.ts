import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IServiceProvider } from '../service-provider.model';

@Component({
  standalone: true,
  selector: 'jhi-service-provider-detail',
  templateUrl: './service-provider-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ServiceProviderDetailComponent {
  @Input() serviceProvider: IServiceProvider | null = null;

  previousState(): void {
    window.history.back();
  }
}
