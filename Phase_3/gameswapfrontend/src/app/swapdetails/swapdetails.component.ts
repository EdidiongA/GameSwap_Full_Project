import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GameswapService } from '../gameswap.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-swapdetails',
  templateUrl: './swapdetails.component.html',
  styleUrls: ['./swapdetails.component.scss']
})
export class SwapdetailsComponent implements OnInit, OnDestroy {
  
  constructor(private router:Router,
    private gameswapService: GameswapService) { }
  //swap details
  proposed
  acceptedRejected
  status
  myRole
  ratingLeft
  //user details
  Nickname
  Distance
  Name
  Email
  Phone
  //Proposed item
  itemNo
  title
  gameType
  condition
  description
  platform
  media
  count
  showPlatform = false
  showMedia = false
  showCount = false
  showDescription = false
  //desired item
  desiredItemNo
  desiredTitle
  desiredGameType
  desiredCondition
  desiredPlatform
  desiredMedia
  desiredCount
  desiredDescription
  showDesiredPlatform = false
  showDesiredMedia = false
  showDesiredCount = false
  showDesiredDescription = true

  showPhone = false

  isRejectedSwap = false

  subscriptionSwapId: Subscription;
  swapId: number;
  ngOnInit(): void {    
    // this.userId = 'user1@gatech.edu';
    this.subscriptionSwapId = this.gameswapService.currentSwapId.subscribe(swapId => this.swapId = swapId);

    const promise = this.gameswapService.getSwapDetails(this.swapId)
    promise.then((data)=>{
      console.log(JSON.stringify(data));                       
      this.proposed = data.proposed_Date
      this.acceptedRejected = data.acknowledged_date
      this.status = data.swap_status
      this.myRole = data.my_role
      this.ratingLeft = data.rating
      this.Nickname = data.nickName
      this.Distance = data.distance
      this.Name = data.firstName 
      this.Email = data.email
      if(data.phone != null)
      {
        if(data.phone.share)
        {
          this.showPhone = true
          this.Phone = data.phone.number + '(' + data.phone.type + ')'
        }
      }

      
      const promise2 = this.gameswapService.getItemDetails(data.desired_Item.id)
      promise2.then((data2)=>{
        this.desiredItemNo = data2.id
        this.desiredCondition = data2.condition
        this.desiredGameType = data2.gameType
        this.desiredTitle = data2.name
        this.desiredDescription =  data2.fullDescription

        this.showDesiredDescription = !(this.desiredDescription == null || this.desiredDescription == '')

        if(this.desiredGameType == 'Video Game')
        {
          this.showDesiredPlatform = true
          this.showDesiredMedia = true
          this.desiredPlatform = data2.gameTypeMetadata.videoGamePlatform
          this.desiredMedia = data2.gameTypeMetadata.videoGameMedia
        }
        else if(this.desiredGameType == 'Computer Game')
        {
          this.showDesiredPlatform = true        
          this.desiredPlatform = data2.gameTypeMetadata.computerGamePlatform        
        }
        else if(this.desiredGameType == 'Jigsaw Puzzle')
        {
          this.showDesiredCount = true        
          this.desiredCount = data2.gameTypeMetadata.jigsawPuzzlePieceCount        
        }
        }
      ).catch((error)=>{
        console.log("Promise rejected with " + JSON.stringify(error));
      });
      

      const promise3 = this.gameswapService.getItemDetails(data.proposed_Item.id)
      promise3.then((data3)=>{
        this.itemNo = data3.id
        this.title = data3.name
        this.gameType = data3.gameType
        this.description = data3.fullDescription
        this.condition = data3.condition
  
        this.showDescription = !(this.description == null || this.description == '')
  
        if(this.gameType == 'Video Game')
        {
          this.showPlatform = true
          this.showMedia = true
          this.platform = data3.gameTypeMetadata.videoGamePlatform
          this.media = data3.gameTypeMetadata.videoGameMedia
        }
        else if(this.gameType == 'Computer Game')
        {
          this.showPlatform = true        
          this.platform = data3.gameTypeMetadata.computerGamePlatform        
        }
        else if(this.gameType == 'Jigsaw Puzzle')
        {
          this.showCount = true        
          this.count = data3.gameTypeMetadata.jigsawPuzzlePieceCount        
        }
      } ).catch((error)=>{
        console.log("Promise rejected with " + JSON.stringify(error));
      });
     

      //if(this.status == 'ACCEPTED')
      //{
        this.isRejectedSwap = true
      //}


    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    });  
    
    this.status = "[Accepted]"
  }
  
  ngOnDestroy(): void {
    this.subscriptionSwapId.unsubscribe()
  }
  onBack() {
    this.router.navigate(['/SwapHistory']);
  }
}
