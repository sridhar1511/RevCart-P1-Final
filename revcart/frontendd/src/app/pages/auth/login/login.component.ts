import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

declare var google: any;

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginData = {
    email: '',
    password: ''
  };
  rememberMe = false;
  showPassword = false;
  isLoading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.checkGoogleCallback();
    this.initializeGoogleSignIn();
    
    // Load remembered email if exists
    const rememberedEmail = localStorage.getItem('rememberedEmail');
    if (rememberedEmail) {
      this.loginData.email = rememberedEmail;
      this.rememberMe = true;
    }
  }

  checkGoogleCallback() {
    this.route.queryParams.subscribe(params => {
      const code = params['code'];
      if (code) {
        this.isLoading = true;
        this.authService.googleCallback(code).subscribe({
          next: (response) => {
            this.router.navigate(['/home']);
            this.isLoading = false;
          },
          error: (error) => {
            console.error('Google callback error:', error);
            alert('Google login failed: ' + (error.error?.message || error.message));
            this.isLoading = false;
          }
        });
      }
    });
  }

  initializeGoogleSignIn() {
    if (typeof google !== 'undefined') {
      google.accounts.id.initialize({
        client_id: '1048739961914-ada23hm8me71ajf7v43pgf0ca23uqhc1.apps.googleusercontent.com',
        callback: (response: any) => this.handleGoogleLogin(response)
      });
      google.accounts.id.renderButton(
        document.getElementById('googleSignInButton'),
        { theme: 'outline', size: 'large' }
      );
    }
  }

  handleGoogleLogin(response: any) {
    if (response.credential) {
      this.isLoading = true;
      this.authService.googleLogin(response.credential).subscribe({
        next: (result) => {
          this.router.navigate(['/home']);
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Google login error:', error);
          alert('Google login failed: ' + (error.error?.message || error.message));
          this.isLoading = false;
        }
      });
    }
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  onLogin() {
    this.isLoading = true;
    
    this.authService.login(this.loginData.email, this.loginData.password).subscribe({
      next: (response) => {
        // Handle remember me functionality
        if (this.rememberMe) {
          localStorage.setItem('rememberedEmail', this.loginData.email);
        } else {
          localStorage.removeItem('rememberedEmail');
        }
        
        this.router.navigate(['/home']);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Login error:', error);
        console.error('Error details:', JSON.stringify(error, null, 2));
        let errorMessage = 'Login failed: ';
        
        if (error.error && error.error.message) {
          errorMessage += error.error.message;
        } else if (error.message) {
          errorMessage += error.message;
        } else if (error.status === 400) {
          errorMessage += 'Invalid credentials';
        } else if (error.status === 0) {
          errorMessage += 'Cannot connect to server. Please check if the backend is running.';
        } else {
          errorMessage += 'An unexpected error occurred';
        }
        
        alert(errorMessage);
        this.isLoading = false;
      }
    });
  }
}
