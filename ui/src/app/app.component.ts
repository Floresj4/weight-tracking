import { Component } from '@angular/core';
import { DataService } from './services/data.service';

interface SelectableYear {
  label: string;
  value: number;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [DataService]
})
export class AppComponent {

  selectableYears: SelectableYear[] = []
  selectedYear: number = 0

  constructor(private data: DataService) {
  }

  ngOnInit() {
    this.getAvailableYears()
  }

  getAvailableYears() {
      //collect years available to review
      this.data.getYearsAvailable().subscribe((response: number[]) => {

        response.forEach((year) => {
          this.selectableYears.push({
            label: `${year}`,
            value: year
          })
        })

        this.selectedYear = this.selectableYears[0].value
      })
  }
}
