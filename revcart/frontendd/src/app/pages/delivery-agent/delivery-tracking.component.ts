import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WebsocketService } from '../../services/websocket.service';
import { DeliveryService } from '../../services/delivery.service';

@Component({
  selector: 'app-delivery-tracking',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './delivery-tracking.component.html',
  styleUrls: ['./delivery-tracking.component.scss']
})
export class DeliveryTrackingComponent implements OnInit, OnDestroy {
  deliveryUpdates: any[] = [];
  currentLocation: any = null;
  isTracking = false;

  constructor(
    private websocketService: WebsocketService,
    private deliveryService: DeliveryService
  ) {}

  ngOnInit() {
    this.websocketService.getDeliveryUpdates().subscribe(update => {
      this.deliveryUpdates.unshift(update);
      this.currentLocation = update;
    });
  }

  startTracking() {
    this.isTracking = true;
    this.getLocation();
  }

  stopTracking() {
    this.isTracking = false;
  }

  getLocation() {
    if (navigator.geolocation) {
      navigator.geolocation.watchPosition(
        (position) => {
          const latitude = position.coords.latitude;
          const longitude = position.coords.longitude;
          this.currentLocation = { latitude, longitude, timestamp: new Date() };
          this.updateDeliveryLocation(latitude, longitude);
        },
        (error) => console.error('Geolocation error:', error)
      );
    }
  }

  updateDeliveryLocation(latitude: number, longitude: number) {
    this.deliveryService.updateDeliveryLocation(latitude, longitude).subscribe({
      next: () => console.log('Location updated'),
      error: (error) => console.error('Error updating location:', error)
    });
  }

  ngOnDestroy() {
    this.websocketService.disconnect();
  }
}
