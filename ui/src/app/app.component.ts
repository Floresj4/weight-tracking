import { Component } from '@angular/core';
import { DataService } from './services/data.service';

interface SelectableYear {
  name: string;
  code: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [DataService]
})
export class AppComponent {

  selectableYears: number[] = []
  selectedYear: number | undefined

  constructor(private data: DataService) {

    this.getAvailableYears()
  }

  getAvailableYears() {
      //collect years available to review
      this.data.getYearsAvailable().subscribe((response: number[]) => {

        response.forEach((v) => {
          this.selectableYears.push(v)
        })

        this.selectedYear = this.selectableYears[0]
      })
  }
}
