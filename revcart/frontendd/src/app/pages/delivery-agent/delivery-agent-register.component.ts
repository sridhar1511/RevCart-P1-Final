import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { DeliveryService } from '../../services/delivery.service';

@Component({
  selector: 'app-delivery-agent-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './delivery-agent-register.component.html',
  styleUrls: ['./delivery-agent-register.component.scss']
})
export class DeliveryAgentRegisterComponent {
  email = '';
  password = '';
  name = '';
  phone = '';
  isLoading = false;
  showPassword = false;

  constructor(private deliveryService: DeliveryService, private router: Router) {}

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  register() {
    if (!this.email || !this.password || !this.name || !this.phone) {
      alert('Please fill in all fields');
      return;
    }

    this.isLoading = true;
    
    // Check if agent already exists
    const existingAgents = JSON.parse(localStorage.getItem('deliveryAgents') || '[]');
    if (existingAgents.some((agent: any) => agent.email === this.email)) {
      alert('Email already registered');
      this.isLoading = false;
      return;
    }
    
    // Create new agent
    const newAgent = {
      id: Date.now(),
      email: this.email,
      password: this.password,
      name: this.name,
      phone: this.phone,
      status: 'AVAILABLE',
      isVerified: true,
      createdAt: new Date()
    };
    
    existingAgents.push(newAgent);
    localStorage.setItem('deliveryAgents', JSON.stringify(existingAgents));
    
    alert('Registration successful! Please login.');
    this.router.navigate(['/delivery-agent/login']);
    this.isLoading = false;
  }
}
