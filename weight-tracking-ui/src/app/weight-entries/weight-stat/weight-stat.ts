import { Component, input } from '@angular/core';

@Component({
  selector: 'app-weight-stat',
  standalone: true,
  imports: [],
  templateUrl: './weight-stat.html',
  styleUrl: './weight-stat.scss'
})
export class WeightStat {

  label = input.required<string>()
  value = input.required<number>()
  date = input<string>()
}
