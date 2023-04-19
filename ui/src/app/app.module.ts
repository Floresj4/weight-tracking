import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TrendDisplayComponent } from './components/trend-display/trend-display.component';
import { YearDisplayComponent } from './components/year-display/year-display.component';

@NgModule({
  declarations: [
    AppComponent,
    TrendDisplayComponent,
    YearDisplayComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
