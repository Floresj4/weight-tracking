import { Component } from '@angular/core';
import { DataService } from './services/data.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [DataService]
})
export class AppComponent {
  title = 'ui';

  year = 2023;
  years: number[] = []
  averageWeight = 159.82
  
  constructor(private data: DataService) {
    data.getYears().subscribe((response) => {
      this.years = response
    })
  }
}
