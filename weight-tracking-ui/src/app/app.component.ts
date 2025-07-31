import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './components/header/header';

import { SAMPLE_ENTRY_DATA } from './model/weight-entries.data';
import { WeightTable } from './components/weight-table/weight-table';
import { WeightEntry } from './model/weight-entry.model';
import { WeightEntryNew } from './weight-entries/weight-entry-new/weight-entry-new';

import { SAMPLE_USERS_DATA } from './model/users.data';
import { UserModel} from './model/user.model';
import { User } from "./components/user/user";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Header, User, WeightTable, WeightEntryNew],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class App {

  selectedEntry = signal<WeightEntry>(<WeightEntry>{})

  weightEntries: WeightEntry[] = SAMPLE_ENTRY_DATA;

  selectedUser = signal<UserModel>(<UserModel>{})

  showNewEntry: boolean = false;

  constructor() {
    this.selectedUser.set(SAMPLE_USERS_DATA[0])
  }

  onAddNewEntry() {
    this.showNewEntry = true
  }

  onNewEntry(entry: WeightEntry) {
    this.showNewEntry = false
    console.log("New entry added: ", entry);
    this.weightEntries.unshift(entry)
  }

  onCloseNewEntry() {
    this.showNewEntry = false
  }

  onSelectedUser(id: string) {
    console.log("User selected: " + id)
  }
}
