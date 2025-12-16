import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { AddressService, Address } from '../../services/address.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  activeTab = 'profile';
  profilePicturePreview: string | null = null;
  isUpdating = false;
  
  user = {
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    profilePicture: null as string | null
  };
  
  addresses: Address[] = [];
  
  showAddressForm = false;
  editingAddress: Address | null = null;
  addressForm: Address = {
    fullName: '',
    phone: '',
    line1: '',
    line2: '',
    city: '',
    state: '',
    pincode: ''
  };
  
  orders = [
    { id: '12345', date: '2024-01-15', items: 5, total: 450, status: 'Delivered' },
    { id: '12346', date: '2024-01-10', items: 3, total: 280, status: 'Out for Delivery' },
    { id: '12347', date: '2024-01-05', items: 7, total: 620, status: 'Delivered' }
  ];
  
  settings = {
    emailNotifications: true,
    smsNotifications: false,
    shareData: true
  };

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private addressService: AddressService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUserData();
    this.loadAddresses();
  }
  
  loadAddresses(): void {
    this.addressService.addresses$.subscribe(addresses => {
      this.addresses = addresses;
    });
  }

  loadUserData(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      this.user.firstName = currentUser.firstName || '';
      this.user.lastName = currentUser.lastName || '';
      this.user.email = currentUser.email || '';
      this.user.phone = currentUser.phone || '';
      this.user.profilePicture = currentUser.profilePicture || null;
      this.profilePicturePreview = currentUser.profilePicture;
    }
  }

  onProfilePictureSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.profilePicturePreview = e.target.result;
        this.user.profilePicture = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  updateProfile(): void {
    this.isUpdating = true;
    const updateData = {
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      phone: this.user.phone,
      profilePicture: this.user.profilePicture
    };

    this.userService.updateUserProfile(updateData).subscribe({
      next: (response) => {
        const updatedUser = this.authService.getCurrentUser();
        updatedUser.firstName = this.user.firstName;
        updatedUser.lastName = this.user.lastName;
        updatedUser.phone = this.user.phone;
        updatedUser.profilePicture = this.user.profilePicture;
        this.authService.setCurrentUser(updatedUser, this.authService.getToken()!);
        alert('Profile updated successfully!');
        this.isUpdating = false;
      },
      error: (error) => {
        console.error('Error updating profile:', error);
        alert('Failed to update profile');
        this.isUpdating = false;
      }
    });
  }
  
  getStatusColor(status: string): string {
    switch (status) {
      case 'Delivered': return 'success';
      case 'Out for Delivery': return 'warning';
      case 'Processing': return 'info';
      default: return 'secondary';
    }
  }
  
  addNewAddress(): void {
    this.editingAddress = null;
    this.addressForm = {
      fullName: '',
      phone: '',
      line1: '',
      line2: '',
      city: '',
      state: '',
      pincode: ''
    };
    this.showAddressForm = true;
  }
  
  editAddress(address: Address): void {
    this.editingAddress = address;
    this.addressForm = { ...address };
    this.showAddressForm = true;
  }
  
  saveAddress(): void {
    if (this.editingAddress) {
      this.addressForm.id = this.editingAddress.id;
    }
    
    this.addressService.saveAddress(this.addressForm).subscribe({
      next: () => {
        this.addressService.loadAddresses();
        this.showAddressForm = false;
        this.editingAddress = null;
      },
      error: (error) => {
        console.error('Error saving address:', error);
        alert('Error saving address');
      }
    });
  }
  
  deleteAddress(address: Address): void {
    if (confirm('Are you sure you want to delete this address?')) {
      this.addressService.deleteAddress(address.id!).subscribe({
        next: () => {
          this.addressService.loadAddresses();
        },
        error: (error) => {
          console.error('Error deleting address:', error);
          alert('Error deleting address');
        }
      });
    }
  }
  
  cancelAddressForm(): void {
    this.showAddressForm = false;
    this.editingAddress = null;
  }

  selectedOrder: any = null;
  showOrderModal = false;

  viewOrderDetails(orderId: string): void {
    this.selectedOrder = this.orders.find(order => order.id === orderId);
    this.showOrderModal = true;
  }

  closeOrderModal(): void {
    this.showOrderModal = false;
    this.selectedOrder = null;
  }
}
