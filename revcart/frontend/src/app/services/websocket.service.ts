import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private notificationSubject = new Subject<any>();
  private deliverySubject = new Subject<any>();
  private stompClient: any;

  constructor(private apiConfig: ApiConfigService) {
    this.initializeWebSocket();
  }

  private initializeWebSocket() {
    const SockJS = (window as any).SockJS;
    const Stomp = (window as any).Stomp;

    if (!SockJS || !Stomp) {
      console.warn('SockJS or Stomp not loaded');
      return;
    }

    const wsUrl = this.apiConfig.getWebSocketUrl();
    const socket = new SockJS(wsUrl);
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, () => {
      this.stompClient.subscribe('/topic/notifications', (message: any) => {
        this.notificationSubject.next(JSON.parse(message.body));
      });
    });
  }

  getNotifications(): Observable<any> {
    return this.notificationSubject.asObservable();
  }

  getDeliveryUpdates(): Observable<any> {
    return this.deliverySubject.asObservable();
  }

  subscribeToOrderUpdates(userId: number) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.subscribe(`/topic/orders/${userId}`, (message: any) => {
        this.notificationSubject.next(JSON.parse(message.body));
      });
    }
  }

  subscribeToDeliveryUpdates(userId: number) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.subscribe(`/topic/delivery/${userId}`, (message: any) => {
        this.deliverySubject.next(JSON.parse(message.body));
      });
    }
  }

  disconnect() {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.disconnect(() => {
        console.log('Disconnected from WebSocket');
      });
    }
  }
}
