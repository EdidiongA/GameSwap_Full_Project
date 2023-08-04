import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GameswapService } from '../gameswap.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-rateswap',
  templateUrl: './rateswap.component.html',
  styleUrls: ['./rateswap.component.scss']
})
export class RateswapComponent implements OnInit {

  userId: string;
  subscriptionUser: Subscription;
  unratedSwapsDataSource = [];
  unratedSwapsDisplayedColumns: string[] = ['acceptedDate', 'role', 'proposedItem', 'desiredItem', 'otherUser', 'rating'];
  ratingValues = [1, 2, 3, 4, 5];

  constructor(private router:Router,
    private gameswapService: GameswapService) { }

  ngOnInit(): void {
    this.subscriptionUser = this.gameswapService.currentUser.subscribe(user => this.userId = user);
    // this.userId = 'user1@gatech.edu';

    this.gameswapService.getUnratedSwaps(this.userId)
    .then((response: any) => {
      for (let unratedSwap of response) {
        this.unratedSwapsDataSource.push({
          swapId: unratedSwap.swap_id,
          acceptedDate: unratedSwap.accepted_date,
          role: unratedSwap.myRole,
          proposedItem: unratedSwap.proposed_item,
          desiredItem: unratedSwap.desired_item,
          otherUser: unratedSwap.other_user,
          otherUserId: unratedSwap.other_user_id
        });
      }

      this.unratedSwapsDataSource = [...this.unratedSwapsDataSource];
    })
    .catch(x => console.log(x));
  }

  onSelectionChange(value) {
    this.gameswapService.updateRating(value.swapId, this.userId, value.rating)
    .then(response => {
      this.unratedSwapsDataSource = [];
      this.ngOnInit();
    })
    .catch(x => console.log(x));
    console.log(value);
  }
    
  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }
}
