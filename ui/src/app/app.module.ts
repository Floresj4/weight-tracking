import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';

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
    HttpClientModule,
    TableModule,
    CardModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
