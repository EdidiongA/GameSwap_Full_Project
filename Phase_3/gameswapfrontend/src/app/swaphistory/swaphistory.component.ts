import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GameswapService } from '../gameswap.service';
import { Subscription } from 'rxjs';

export interface SwapSummary {
  role: string;
  total: number;
  accepted: number;
  rejected: number;
  rejectedPercentage: string;
}

export interface SwapHistory {
  proposedDate: string,
  acknowledgedDate: string,
  status: string,
  role: string,
  proposedItem: string,
  desiredItem: string,
  otherUser: string,
  rating: number,
  ratingDropdownClass: string,
  swapId: number,
  detail: string
}

@Component({
  selector: 'app-swaphistory',
  templateUrl: './swaphistory.component.html',
  styleUrls: ['./swaphistory.component.scss']
})

export class SwaphistoryComponent implements OnInit {

  userId: string;
  swapId: number;
  subscriptionUser: Subscription;
  subscriptionSwapId: Subscription;
  swapSummaryDisplayedColumns: string[] = ['role', 'total', 'accepted', 'rejected', 'rejectedPercentage'];
  swapSummaryDataSource = [];
  swapHistoryDisplayedColumns: string[] = ['proposedDate', 'acknowledgedDate', 'status', 'role', 'proposedItem', 'desiredItem', 'otherUser', 'rating', 'detail'];
  swapHistoryDataSource = [];
  ratingValues = [1, 2, 3, 4, 5];
  ratingValue: number;

  constructor(private router:Router,
    private gameswapService: GameswapService) { }

  ngOnInit(): void {
    this.subscriptionUser = this.gameswapService.currentUser.subscribe(user => this.userId = user);
    // this.userId = 'user1@gatech.edu';
    this.subscriptionSwapId = this.gameswapService.currentSwapId.subscribe(swapId => this.swapId = swapId);

    this.gameswapService.getSwapHistory(this.userId)
    .then((response: any) => {
      let proposerSummary = response.proposer_summary;
      this.swapSummaryDataSource.push({
        role: proposerSummary.my_role,
        total: proposerSummary.total_count,
        accepted: proposerSummary.accepted_count,
        rejected: proposerSummary.rejected_count,
        rejectedPercentage: (proposerSummary.rejected_percentage && proposerSummary.rejected_percentage != 'NaN') ? Number(proposerSummary.rejected_percentage).toFixed(1) + '%' : '0.0%',
      });

      let counterpartySummary = response.counterparty_summary;
      this.swapSummaryDataSource.push({
        role: counterpartySummary.my_role,
        total: counterpartySummary.total_count,
        accepted: counterpartySummary.accepted_count,
        rejected: counterpartySummary.rejected_count,
        rejectedPercentage: (counterpartySummary.rejected_percentage && counterpartySummary.rejected_percentage != 'NaN') ? Number(counterpartySummary.rejected_percentage).toFixed(1) + '%' : '0.0%',
      });

      let history = response.history;
      for (let entry of history) {
        let ratingCellDetails = this.getRatingCellDetails(entry.swap_status, entry.rating);

        this.swapHistoryDataSource.push({
          proposedDate: entry.proposed_Date,
          acknowledgedDate: entry.acknowledged_date,
          status: [entry.swap_status.slice(0, 1).toUpperCase(), entry.swap_status.slice(1).toLowerCase()].join(''),
          role: entry.my_role,
          proposedItem: entry.proposed_item,
          desiredItem: entry.desired_item,
          otherUser: entry.other_user,
          otherUserId: entry.other_user_id,
          rating: ratingCellDetails.rating,
          ratingDropdownClass: ratingCellDetails.ratingDropdownClass,
          swapId: entry.swap_id,
          detail: 'Detail',
        });
      }

      this.swapSummaryDataSource = [...this.swapSummaryDataSource];
      this.swapHistoryDataSource = [...this.swapHistoryDataSource];
    })
    .catch(x => console.log(x));
  }

  ngAfterContentChecked() {
    let rejectedPercentageCells = document.querySelectorAll("td.mat-column-rejectedPercentage");
    if (rejectedPercentageCells.length > 0) {
      for (let i = 0; i < rejectedPercentageCells.length; i++) {
        let value = rejectedPercentageCells[i].innerHTML;
        if (Number(value.substring(0, value.length - 1)) >= 50) {
          rejectedPercentageCells[i].classList.add('highlight');
        } else {
          rejectedPercentageCells[i].classList.remove('highlight');
        }
      }
    }
  }

  getRatingCellDetails(swapStatus, rating) {
    return {
      ratingDropdownClass: (swapStatus == 'REJECTED' || rating != '0.00') ? 'hidden' : '',
      rating: (swapStatus == 'ACCEPTED' && rating != '0.00') ? rating : ''
    };
  }
  
  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }

  onDetails(element) {
    console.log(element);
    this.swapId = element.swapId;
    this.gameswapService.updateSelectedSwapId(this.swapId)
    this.router.navigate(['/SwapDetails']);
  }

  onSelectionChange(value) {
    this.gameswapService.updateRating(value.swapId, this.userId, value.rating)
    .then(response => {
      this.swapHistoryDataSource = [];
      this.swapSummaryDataSource = [];
      this.ngOnInit();
    })
    .catch(x => console.log(x));
    console.log(value);
  }
}
