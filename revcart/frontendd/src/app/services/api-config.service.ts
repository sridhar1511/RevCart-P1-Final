import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiConfigService {
  private apiBaseUrl: string;

  constructor() {
    this.apiBaseUrl = this.getApiBaseUrl();
  }

  private getApiBaseUrl(): string {
    // In Docker, use nginx proxy; in development, use direct backend
    if (window.location.port === '80') {
      return window.location.origin; // Use nginx proxy
    }
    return 'http://localhost:8081';
  }

  getApiUrl(endpoint: string): string {
    return `${this.getApiBaseUrl()}/api/${endpoint}`;
  }

  getBaseUrl(): string {
    return this.apiBaseUrl;
  }

  getWebSocketUrl(): string {
    const hostname = window.location.hostname;
    const port = 8081;
    const wsProtocol = window.location.protocol === 'https:' ? 'wss' : 'ws';

    if (hostname === 'localhost' || hostname === '127.0.0.1') {
      return `${wsProtocol}://localhost:${port}/ws-notifications`;
    } else {
      return `${wsProtocol}://${hostname}:${port}/ws-notifications`;
    }
  }
}
