import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './component/header/header';

import { SAMPLE_ENTRY_DATA } from './model/weight-entry.data';
import { WeightEntry } from './model/weight-entry.model';

const randomIndex = Math.floor(Math.random() * SAMPLE_ENTRY_DATA.length)

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Header],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class App {
  selectedEntry: WeightEntry = SAMPLE_ENTRY_DATA[randomIndex]

  onNewEntry() {
    console.log('click')
  }
}
