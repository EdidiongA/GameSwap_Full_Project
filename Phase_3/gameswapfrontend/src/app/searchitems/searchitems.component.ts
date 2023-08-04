import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {SelectionModel} from "@angular/cdk/collections";
import { GameSwapItem } from '../models';
import { GameswapService } from '../gameswap.service';
import { Subscription } from 'rxjs';
import { MatTableDataSource } from '@angular/material/table';
const queryParamName = "pippo";

@Component({
  selector: 'app-searchitems',
  templateUrl: './searchitems.component.html',
  styleUrls: ['./searchitems.component.scss']
})
export class SearchitemsComponent implements OnInit, OnDestroy {

  
ELEMENT_DATA: GameSwapItem[] = [ ];

  searchResult
  item: GameSwapItem
  subscriptionItem: Subscription;
  keyword
  miles
  inpostal
  searchSelected
  displayedColumns = ['Item #', 'Game Type', 'Title', 'Condition', 'Description', 'Distance', 'Details'];
  dataSource = new MatTableDataSource<GameSwapItem>(this.ELEMENT_DATA);
  selection: SelectionModel<GameSwapItem> = new SelectionModel<GameSwapItem>(false, []);
  
  constructor(private router:Router,
    private gameswapService: GameswapService) {

  }

  ngOnInit():void{

    this.subscriptionItem = this.gameswapService.currentItem.subscribe(item => this.item = this.item)


    this.ELEMENT_DATA.splice(0,this.ELEMENT_DATA.length);   

    // ELEMENT_DATA.push(
    //   {
    //     itemId: 1,
    //     type: 'video game',
    //     title: 'tetris',
    //     condition: 'good',
    //     description: '',
    //     distance: 0.0,
    //     details: 'details',
    //     selected: false
    //   })   
    //   ELEMENT_DATA.push(
    //     {
    //       itemId: 2,
    //       type: 'jigsaw puzzle',
    //       title: 'Georgia Tech',
    //       condition: 'mint',
    //       description: 'blah blah',
    //       distance: 10.8,
    //       details: 'details',
    //       selected: false
    //     })   
           
    // this.dataSource = ELEMENT_DATA;
  }

  ngOnDestroy() {
    this.subscriptionItem.unsubscribe()
  }

  selectRow($event: any, row:Element){
    console.info("clicked", $event);
    console.info(row); 
  }

  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }  
 
  searchString: string;

  onSearch() {
    console.log(this.searchSelected)
    var searchKey = ""
    if(this.searchSelected == 'Bykeyword')
    {        
      this.searchString = 'Keyword'
      searchKey = this.keyword      
    }
    else if(this.searchSelected == 'Bymypostal')
    {
      this.searchString = 'MyPostalCode'       
    }
    else if(this.searchSelected == 'Bymiles')
    {
      this.searchString = 'Miles'
      searchKey = this.miles
    }
    else if(this.searchSelected == 'Byinpostal')
    {
      this.searchString = 'PostalCode'
      searchKey = this.inpostal
    }
        
    this.searchResult = this.searchString + ' "'+ searchKey + '"'
    
    const promise = this.gameswapService.searchItem(this.searchString, searchKey)
    promise.then((data)=>{
      console.log(JSON.stringify(data));

      this.ELEMENT_DATA.splice(0,this.ELEMENT_DATA.length);

      if(data.length == 0)
      {
        this.searchResult = 'Sorry, no results found!'        
      }
      else
      {
        data.forEach(element => {
          let titleClass = "";
          let descriptionClass = "";
          if (this.searchSelected == 'Bykeyword') {
            if (element['item'].name.toLowerCase().includes(searchKey.toLowerCase())) {
              titleClass = 'highlight';
            }
            if (element['item'].description && element['item'].description.toLowerCase().includes(searchKey.toLowerCase())) {
              descriptionClass = 'highlight';
            }
          }
          this.ELEMENT_DATA.push(
            {
              itemId: element['item'].id,
              type: element['item'].gameType,
              title: element['item'].name,
              titleClass,
              condition: element['item'].condition,
              description: element['item'].description,
              descriptionClass,
              distance: element.distance,
              details: 'details',
              selected: false
            })   
          
        });
      }
      this.dataSource = new MatTableDataSource<GameSwapItem>(this.ELEMENT_DATA);
    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    }); 
    console.log(this.searchString)
  }

  onDetails(element) {
    //console.log(element)    
    this.item = element
    console.log(this.item)    
    this.gameswapService.updateSelectedItem(this.item);
    this.router.navigate(['/ViewItem']);
  }
 
}