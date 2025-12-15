import 'zone.js';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';

// Suppress Chrome extension errors
window.addEventListener('error', (event) => {
  if (event.message?.includes('listener indicated an asynchronous response') ||
      event.message?.includes('message channel closed') ||
      event.filename?.includes('content_script') ||
      event.filename?.includes('extension')) {
    event.preventDefault();
  }
});

// Suppress unhandled promise rejections from extensions
window.addEventListener('unhandledrejection', (event) => {
  if (event.reason?.message?.includes('listener indicated an asynchronous response') ||
      event.reason?.message?.includes('message channel closed') ||
      event.reason?.stack?.includes('extension') ||
      event.reason?.stack?.includes('content_script')) {
    event.preventDefault();
  }
});

bootstrapApplication(AppComponent, appConfig)
  .catch(err => console.error(err));
