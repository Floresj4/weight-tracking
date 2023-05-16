import { Component, Input } from '@angular/core';

import { WeightEntries } from 'src/app/model/weight-monthly-avg';

@Component({
  selector: 'app-table-display',
  templateUrl: './table-display.component.html',
  styleUrls: ['./table-display.component.scss']
})
export class TableDisplayComponent {

  @Input('display')
  weights: WeightEntries = <WeightEntries>{}
}
