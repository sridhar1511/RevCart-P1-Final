import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { DeliveryService } from '../../services/delivery.service';

@Component({
  selector: 'app-delivery-agent-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './delivery-agent-login.component.html',
  styleUrls: ['./delivery-agent-login.component.scss']
})
export class DeliveryAgentLoginComponent {
  email = '';
  password = '';
  otp = '';
  isLoading = false;
  showPassword = false;
  showOtpForm = false;
  otpMessage = '';

  constructor(private deliveryService: DeliveryService, private router: Router) {}

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  login() {
    if (!this.email || !this.password) {
      alert('Please fill in all fields');
      return;
    }

    this.isLoading = true;
    this.deliveryService.agentLogin(this.email, this.password).subscribe({
      next: (response) => {
        console.log('Login response:', response);
        localStorage.setItem('deliveryAgent', JSON.stringify(response.agent || response));
        // Trigger header refresh
        window.dispatchEvent(new Event('storage'));
        alert('Login successful!');
        this.router.navigate(['/delivery-agent/dashboard']);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Login error:', error);
        this.isLoading = false;
        
        if (error.error?.requiresVerification) {
          this.showOtpForm = true;
          this.otpMessage = error.error.message || 'Please verify your email with OTP';
        } else {
          alert(error.error?.message || 'Invalid credentials');
        }
      }
    });
  }

  verifyOtp() {
    if (!this.otp) {
      alert('Please enter OTP');
      return;
    }

    this.isLoading = true;
    this.deliveryService.verifyAgentOtp(this.email, this.otp).subscribe({
      next: (response) => {
        alert('Email verified successfully! Logging you in...');
        this.showOtpForm = false;
        this.otp = '';
        this.otpMessage = '';
        // Auto-login after verification
        this.loginAfterVerification();
      },
      error: (error) => {
        alert(error.error?.message || 'OTP verification failed');
        this.isLoading = false;
      }
    });
  }

  loginAfterVerification() {
    this.deliveryService.agentLogin(this.email, this.password).subscribe({
      next: (response) => {
        console.log('Agent login response:', response);
        localStorage.setItem('deliveryAgent', JSON.stringify(response.agent));
        this.router.navigate(['/delivery-agent/dashboard']);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Login error:', error);
        alert('Login failed after verification. Please try logging in again.');
        this.isLoading = false;
      }
    });
  }

  resendOtp() {
    this.isLoading = true;
    this.deliveryService.resendAgentOtp(this.email).subscribe({
      next: (response) => {
        alert('OTP resent to your email');
        this.isLoading = false;
      },
      error: (error) => {
        alert(error.error?.message || 'Failed to resend OTP');
        this.isLoading = false;
      }
    });
  }
}
