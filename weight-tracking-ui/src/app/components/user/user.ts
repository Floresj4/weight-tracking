import { Component, computed, input } from '@angular/core';

import { UserModel } from '../../model/user.model';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [],
  templateUrl: './user.html',
  styleUrl: './user.scss'
})
export class User {

    // @Input({ required: true }) user!: UserModel
    user = input.required<UserModel>()

    imagePath = computed(() => '/' + this.user().avatar )
}
