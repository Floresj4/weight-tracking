import { Component, computed, Input } from '@angular/core';

import { UserModel } from '../../model/user.model';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [],
  templateUrl: './user.html',
  styleUrl: './user.scss'
})
export class User {

    @Input({ required: true }) user!: UserModel

    imagePath = computed(() => '/' + this.user.avatar )
}
