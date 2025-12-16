import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerData = {
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    address: ''
  };
  confirmPassword = '';
  agreeTerms = false;
  showPassword = false;
  showConfirmPassword = false;
  isLoading = false;
  
  // OTP verification
  showOtpVerification = false;
  otp = '';
  otpLoading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPassword() {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  onRegister() {
    if (!this.registerData.firstName.trim() || !this.registerData.lastName.trim()) {
      alert('First name and last name are required');
      return;
    }
    if (this.registerData.password !== this.confirmPassword) {
      alert('Passwords do not match');
      return;
    }
    
    this.isLoading = true;
    
    const userData = {
      name: `${this.registerData.firstName.trim()} ${this.registerData.lastName.trim()}`,
      email: this.registerData.email.trim(),
      password: this.registerData.password,
      phone: this.registerData.phone.trim(),
      address: this.registerData.address.trim()
    };
    
    this.authService.register(userData).subscribe({
      next: (response) => {
        this.showOtpVerification = true;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Registration error:', error);
        alert(error.error?.message || 'Registration failed. Please try again.');
        this.isLoading = false;
      }
    });
  }

  verifyOtp() {
    if (!this.otp || this.otp.length !== 6) {
      alert('Please enter a valid 6-digit OTP');
      return;
    }

    this.otpLoading = true;
    this.authService.verifyOtp(this.registerData.email, this.otp).subscribe({
      next: (response) => {
        alert('Email verified successfully! You can now login.');
        this.router.navigate(['/login']);
        this.otpLoading = false;
      },
      error: (error) => {
        console.error('OTP verification error:', error);
        alert(error.error?.message || 'OTP verification failed');
        this.otpLoading = false;
      }
    });
  }

  resendOtp() {
    this.authService.sendOtp(this.registerData.email).subscribe({
      next: (response) => {
        alert('OTP sent to your email');
      },
      error: (error) => {
        console.error('Resend OTP error:', error);
        alert('Failed to resend OTP: ' + (error.error?.message || error.message));
      }
    });
  }
}
