import { Component, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WeightEntry } from '../../model/weight-entry.model';

@Component({
  selector: 'app-weight-new',
  standalone: true,
  imports: [ FormsModule],
  templateUrl: './weight-entry-new.html',
  styleUrl: './weight-entry-new.scss'
})
export class WeightEntryNew {

  close = output<void>()

  enteredWeight = signal(0)
  enteredDate = signal('')

  onSave() {

    const weight: WeightEntry = {
      value: this.enteredWeight(),
      date: new Date(this.enteredDate()).toUTCString()
    }

    console.log('Saving weight entry', weight)
    this.close.emit();
  }

  onClose() {
    this.close.emit()
  }
}
