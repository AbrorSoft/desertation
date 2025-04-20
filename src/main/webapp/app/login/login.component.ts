import { Component, ViewChild, OnInit, AfterViewInit, ElementRef, inject, signal } from '@angular/core';
import { FormGroup, FormControl, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-login',
  standalone: true,
  imports: [SharedModule, FormsModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
})
export default class LoginComponent implements OnInit, AfterViewInit {
  @ViewChild('username', { static: false })
  username!: ElementRef;
  loginDisabled = false;
  countdown = 30;
  intervalId: any;
  authenticationError = signal(false);

  loginForm = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    rememberMe: new FormControl(false, { nonNullable: true, validators: [Validators.required] }),
  });
  // Add to component class
  showPassword = false;

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }
  private accountService = inject(AccountService);
  private loginService = inject(LoginService);
  private router = inject(Router);

  ngOnInit(): void {
    // if already authenticated then navigate to home page
    this.checkLoginErrors();
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']);
      }
    });
  }

  ngAfterViewInit(): void {
    this.username.nativeElement.focus();
  }
  checkLoginErrors(): void {
    const errorCount = Number(localStorage.getItem('loginErrors')) || 0;

    if (errorCount >= 3) {
      this.loginDisabled = true;
      this.startCountdown();
    }
  }
  startCountdown(): void {
    this.countdown = 30;
    this.intervalId = setInterval(() => {
      this.countdown--;
      if (this.countdown <= 0) {
        clearInterval(this.intervalId);
        localStorage.setItem('loginErrors', '0');
        this.loginDisabled = false;
      }
    }, 1000);
  }
  login(): void {
    this.loginService.login(this.loginForm.getRawValue()).subscribe({
      next: () => {
        this.authenticationError.set(false);
        localStorage.removeItem('loginErrors');

        if (!this.router.getCurrentNavigation()) {
          this.router.navigate(['']);
        }
      },
      error: () => {
        this.authenticationError.set(true);

        const errorCount = Number(localStorage.getItem('loginErrors')) || 0;
        const updatedCount = errorCount + 1;
        localStorage.setItem('loginErrors', updatedCount.toString());

        if (updatedCount >= 3) {
          this.loginDisabled = true;
          this.startCountdown();
        }
      },
    });
  }
}
