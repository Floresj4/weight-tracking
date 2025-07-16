import { Component } from '@angular/core';
import { Weight } from './weight/weight';

@Component({
  selector: 'app-weight-table',
  standalone: true,
  imports: [ Weight],
  templateUrl: './weight-table.html',
  styleUrl: './weight-table.scss'
})
export class WeightTable {

}
