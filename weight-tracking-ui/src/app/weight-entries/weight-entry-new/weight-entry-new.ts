import { Component, output } from '@angular/core';

@Component({
  selector: 'app-weight-new',
  standalone: true,
  imports: [],
  templateUrl: './weight-entry-new.html',
  styleUrl: './weight-entry-new.scss'
})
export class WeightEntryNew {

  close = output<void>()

  onSave() {
    this.close.emit();
  }

  onClose() {
    this.close.emit()
  }
}
