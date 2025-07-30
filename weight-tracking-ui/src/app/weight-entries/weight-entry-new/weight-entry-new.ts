import { Component, output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-weight-new',
  standalone: true,
  imports: [ FormsModule],
  templateUrl: './weight-entry-new.html',
  styleUrl: './weight-entry-new.scss'
})
export class WeightEntryNew {

  close = output<void>()

  enteredWeight: number | null = null;
  enteredDate: string | null = null;

  onSave() {
    this.close.emit();
  }

  onClose() {
    this.close.emit()
  }
}
