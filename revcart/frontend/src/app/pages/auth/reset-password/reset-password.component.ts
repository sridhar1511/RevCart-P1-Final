import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
  token = '';
  newPassword = '';
  confirmPassword = '';
  isLoading = false;
  message = '';
  messageType = '';
  showPassword = false;

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
      if (!this.token) {
        this.message = 'Invalid reset link';
        this.messageType = 'error';
      }
    });
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  resetPassword() {
    if (!this.newPassword || !this.confirmPassword) {
      this.message = 'Please fill in all fields';
      this.messageType = 'error';
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      this.message = 'Passwords do not match';
      this.messageType = 'error';
      return;
    }

    if (this.newPassword.length < 6) {
      this.message = 'Password must be at least 6 characters';
      this.messageType = 'error';
      return;
    }

    this.isLoading = true;
    this.authService.resetPassword(this.token, this.newPassword).subscribe({
      next: (response) => {
        this.message = 'Password reset successfully. Redirecting to login...';
        this.messageType = 'success';
        this.isLoading = false;
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (error) => {
        this.message = error.error?.message || 'Error resetting password';
        this.messageType = 'error';
        this.isLoading = false;
      }
    });
  }
}
