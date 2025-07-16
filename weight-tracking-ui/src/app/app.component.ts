import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './components/header/header';

import { SAMPLE_ENTRY_DATA } from './model/weight-entries.data';
import { WeightTable } from './components/weight-table/weight-table';
import { WeightEntry } from './model/weight-entry.model';

import { SAMPLE_USERS_DATA } from './model/users.data';
import { UserModel} from './model/user.model';
import { User } from "./components/user/user";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Header, User, WeightTable],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class App {

  randomIndex: number = 0
  selectedEntry = signal<WeightEntry>(<WeightEntry>{})

  weightEntries: WeightEntry[] = SAMPLE_ENTRY_DATA;

  selectedUser = signal<UserModel>(<UserModel>{})

  constructor() {
    this.onNewEntry()

    this.selectedUser.set(SAMPLE_USERS_DATA[0])
  }

  onNewEntry() {
    this.randomIndex = Math.floor(Math.random() * SAMPLE_ENTRY_DATA.length)
    this.selectedEntry.set(SAMPLE_ENTRY_DATA[this.randomIndex])
  }

  onSelectedUser(id: string) {
    console.log("User selected: " + id)
  }
}
