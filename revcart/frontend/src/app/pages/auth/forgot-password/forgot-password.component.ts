import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent {
  email = '';
  isLoading = false;
  message = '';
  messageType = '';

  constructor(private authService: AuthService, private router: Router) {}

  sendResetLink() {
    if (!this.email) {
      this.message = 'Please enter your email';
      this.messageType = 'error';
      return;
    }

    this.isLoading = true;
    this.authService.forgotPassword(this.email).subscribe({
      next: (response) => {
        this.message = 'Password reset link sent to your email';
        this.messageType = 'success';
        this.isLoading = false;
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (error) => {
        this.message = error.error?.message || 'Error sending reset link';
        this.messageType = 'error';
        this.isLoading = false;
      }
    });
  }
}
