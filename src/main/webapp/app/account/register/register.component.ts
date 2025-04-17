import { AfterViewInit, Component, ElementRef, inject, signal, ViewChild } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/config/error.constants';
import SharedModule from 'app/shared/shared.module';
import PasswordStrengthBarComponent from '../password/password-strength-bar/password-strength-bar.component';
import { RegisterService } from './register.service';

@Component({
  selector: 'jhi-register',
  standalone: true,
  styles: `
    .registration-page {
      display: flex;
      min-height: 100vh; /* Ensure full viewport height */
    }

    .registration-side-content {
      flex: 0 0 40%; /* Adjust width as needed */
      background-image: url('https://avatars.mds.yandex.net/i?id=2ac25139f516b34458eb4972224d53d67acfc905-4544610-images-thumbs&n=13'); /* Replace with your image */
      background-size: cover;
      background-position: center;
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      text-align: center;
    }

    .registration-side-content .overlay {
      background-color: rgba(0, 0, 0, 0.5); /* Optional overlay for better text readability */
      padding: 2rem;
      border-radius: 8px;
    }

    .registration-form-container {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 2rem;
    }

    .registration-form-wrapper {
      width: 100%;
      max-width: 400px; /* Adjust max width as needed */
      padding: 2rem;
      border-radius: 8px;
      /* Optional: Add a subtle box shadow */
      /* box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); */
    }

    .modern-input {
      border: 1px solid #ccc;
      border-radius: 4px;
      padding: 0.75rem;
      font-size: 1rem;
    }

    .modern-input:focus {
      border-color: #007bff; /* Your primary color */
      outline: none;
      box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25); /* Optional focus ring */
    }

    .modern-button {
      background-color: #007bff; /* Your primary color */
      color: white;
      border: none;
      border-radius: 4px;
      padding: 0.75rem 1.5rem;
      font-size: 1rem;
      cursor: pointer;
      transition: background-color 0.3s ease;
    }

    .modern-button:hover {
      background-color: #0056b3; /* Darker shade of primary color */
    }

    .modern-alert {
      border-radius: 4px;
      padding: 0.75rem;
      margin-bottom: 1rem;
    }

    .modern-link {
      color: #007bff;
      text-decoration: none;
    }

    .modern-link:hover {
      text-decoration: underline;
    }

    .password-input-group {
      position: relative;
      display: flex;
      align-items: center;
    }

    .password-input-group .form-control {
      border-right: none;
      border-top-right-radius: 0;
      border-bottom-right-radius: 0;
    }

    .password-input-group .password-toggle-button {
      position: absolute;
      right: 0.5rem;
      top: 50%;
      transform: translateY(-50%);
      background: none;
      border: none;
      cursor: pointer;
      padding: 0.5rem;
      outline: none;
    }

    /* Add styling for your password strength bar as well */
  `,
  imports: [SharedModule, RouterModule, FormsModule, ReactiveFormsModule, PasswordStrengthBarComponent],
  templateUrl: './register.component.html',
})
export default class RegisterComponent implements AfterViewInit {
  @ViewChild('login', { static: false })
  login?: ElementRef;

  doNotMatch = signal(false);
  error = signal(false);
  errorEmailExists = signal(false);
  errorUserExists = signal(false);
  success = signal(false);

  registerForm = new FormGroup({
    login: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
  });

  private translateService = inject(TranslateService);
  private registerService = inject(RegisterService);
  private router = inject(Router);

  ngAfterViewInit(): void {
    if (this.login) {
      this.login.nativeElement.focus();
    }
  }

  register(): void {
    this.doNotMatch.set(false);
    this.error.set(false);
    this.errorEmailExists.set(false);
    this.errorUserExists.set(false);

    const { password, confirmPassword } = this.registerForm.getRawValue();
    if (password !== confirmPassword) {
      this.doNotMatch.set(true);
    } else {
      const { login, email } = this.registerForm.getRawValue();
      this.registerService.save({ login, email, password, langKey: this.translateService.currentLang }).subscribe({
        next: () => {
          this.success.set(true);
          this.router.navigate(['/login']).then(r => r);
        },
        error: response => this.processError(response),
      });
    }
  }
  // In your component class
  showPassword: boolean = false;
  passwordFieldType: string = 'password';

  togglePasswordVisibility(fieldId: string): void {
    // Toggle the visibility state
    this.showPassword = !this.showPassword;

    // Update the input type
    this.passwordFieldType = this.showPassword ? 'text' : 'password';

    // Optional: Toggle icon state if using different icons
    const icon = document.querySelector(`#${fieldId}-icon`);
    if (icon) {
      icon.classList.toggle('bi-eye');
      icon.classList.toggle('bi-eye-slash');
    }
  }
  private processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists.set(true);
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists.set(true);
    } else {
      this.error.set(true);
    }
  }
}
