import { Component, OnInit, OnDestroy } from '@angular/core';
import { GameswapService } from '../gameswap.service';
import { GameSwapItem } from '../models';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-viewacceptrejectitem',
  templateUrl: './viewacceptrejectitem.component.html',
  styleUrls: ['./viewacceptrejectitem.component.scss']
})
export class ViewacceptrejectitemComponent implements OnInit, OnDestroy {

  showGreen = false
  showRed = false
  showYellow = false
  showOrange = false
  count
  media
  title
  type
  platform
  condition  
  offerBy
  location
  rating
  item: GameSwapItem
  subscriptionItem: Subscription;
  showPlatform = false
  showMedia = false
  showCount = false
  showDescription = true
  description
  userId: string;
  subscriptionUser: Subscription;
  secondColumnClass = "";
  constructor(private gameswapService: GameswapService,private router:Router) { }

  ngOnInit(): void {
    this.subscriptionUser = this.gameswapService.currentUser.subscribe(user => this.userId = user);
    this.subscriptionItem = this.gameswapService.currentAcceptRejectItemForViewDetails.subscribe(item => this.item = item)
    const promise = this.gameswapService.getItemDetails(this.item.itemId)
    promise.then((data)=>{     
      this.secondColumnClass = this.userId == data.itemOwner.user.email ? "hidden" : "";
      console.log(JSON.stringify(data));                       
      this.title = data.name
      this.condition = data.condition
      this.type = data.gameType
      this.media = "NA"
      this.platform = "NA"
      this.count = "NA"
      this.description = data.fullDescription
      if(this.description == null || this.description == '')
      {
        this.showDescription = false
      }

      if(this.type == "Computer Game")
      {
        this.platform = data.gameTypeMetadata.computerGamePlatform
        this.showPlatform = true
      }
      else if(this.type == "Video Game")
      {
        this.platform = data.gameTypeMetadata.videoGamePlatform
        this.media = data.gameTypeMetadata.videoGameMedia
        this.showPlatform = true
        this.showMedia = true
      }
      else if(this.type == "Jigsaw Puzzle")
      {
        this.count = data.gameTypeMetadata.jigsawPuzzlePieceCount        
        this.showCount = true
      }
     
      if(this.item.distance > 0 && this.item.distance < 25)
      {
        this.showGreen = true
      }
      else if(this.item.distance >= 25 && this.item.distance < 50)
      {
        this.showYellow = true
      }
      else if(this.item.distance >= 50 && this.item.distance < 100)
      {
        this.showOrange = true
      }
      else
      {
        this.showRed = true
      }
      
      this.offerBy = data.itemOwner.user.nickName;
      this.location = data.itemOwner.user.location.city + "," + data.itemOwner.user.location.state + " " + data.itemOwner.user.location.postalCode;
      this.rating = 'None'
      if(data.itemOwner.rating != null)
      {
        this.rating = data.itemOwner.rating;
      }
    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    });  
  }

  ngOnDestroy() {
    this.subscriptionItem.unsubscribe()
  }

  onBack() {
    this.router.navigate(['/AcknowledgeSwap']);
  }

}
