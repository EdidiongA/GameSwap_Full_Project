import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { GameSwapItem } from './models';
import { BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class GameswapService {
  // chrome.exe --user-data-dir="C://Chrome dev session" --disable-web-security
  rootURL = 'http://localhost:8081/GameSwap/';

  
  private selectedItem: BehaviorSubject<GameSwapItem> = new BehaviorSubject<GameSwapItem>(null);
  currentItem = this.selectedItem.asObservable();

  private selectedSwapId: BehaviorSubject<number> = new BehaviorSubject<number>(null);
  currentSwapId = this.selectedSwapId.asObservable();

  private selectedAcceptRejectItemForViewDetails: BehaviorSubject<GameSwapItem> = new BehaviorSubject<GameSwapItem>(null);
  currentAcceptRejectItemForViewDetails = this.selectedAcceptRejectItemForViewDetails.asObservable();

  private selectedMytemForViewDetails: BehaviorSubject<GameSwapItem> = new BehaviorSubject<GameSwapItem>(null);
  currentMyItemForViewDetails = this.selectedMytemForViewDetails.asObservable();


  private userId: BehaviorSubject<string> = new BehaviorSubject<string>(null);
  currentUser = this.userId.asObservable();

  currentUserId
  updateUserId(userId: string) {
    this.userId.next(userId)     
    this.currentUserId = userId;
  }

  updateSelectedItem(item: GameSwapItem) {
    this.selectedItem.next(item);  
  }

  updateSelectedSwapId(swapId: number) {
    this.selectedSwapId.next(swapId);
  }

  updateSelectedAcceptRejectItemForViewDetails(item: GameSwapItem) {
    this.selectedAcceptRejectItemForViewDetails.next(item)     
  }

  updateSelectedMyItemForViewDetails(item: GameSwapItem) {
    this.selectedMytemForViewDetails.next(item)     
  }

  constructor(private http: HttpClient) { }  

  addUser(email, password, firstName, lastName, nickname, postalCode, phoneNumber, type, isShowPhoneNumberChecked) {
    console.log('Add user');
    let requestBody = {
      email,
      firstName,
      lastName,
      location: {
        postalCode
      },
      nickName: nickname,
      password,
      phone: {
        number: phoneNumber,
        share: !!isShowPhoneNumberChecked,
        type
      }
    }

    return this.http.post<any>(this.rootURL + 'user', requestBody).toPromise();
  }

  
  searchItem(keyword, searchValue) {
    if(keyword == 'Miles')
    {
      searchValue = Number(searchValue)
    }
    else if(keyword == 'PostalCode')
    {
      searchValue = Number(searchValue)
    }
    return this.http.get<any>(this.rootURL + 'item/search', {
      headers: {
        email: this.currentUserId,
      },
      params: {        
        searchKey: keyword,
        searchValue: searchValue
           }
    }).toPromise()
  }

  getAcceptRejectSwap() {    
    return this.http.get<any>(this.rootURL + 'swap/acceptrejectswap', {
      params: {
        userID: this.currentUserId,
           }
    }).toPromise()
  }

  getvideoGamePlatforms() {    
    return this.http.get<any>(this.rootURL + 'item/videoGamePlatforms').toPromise()
  }

  getItemDetails(itemId) {    
    return this.http.get<any>(this.rootURL + 'item/' + itemId, {
      headers: {
        email: this.currentUserId,
      },
    }).toPromise()
  }

  postListItem(condition, description,gameType,  computerGamePlatform, jigsawPuzzlePieceCount, videoGameMedia, videoGamePlatform, name) {   

    let body = {
      "condition": condition,
      "description": description,
      "gameType": gameType,
      "gameTypeMetadata": {
        "computerGamePlatform": computerGamePlatform,
        "jigsawPuzzlePieceCount": jigsawPuzzlePieceCount,
        "videoGameMedia": videoGameMedia,
        "videoGamePlatform": videoGamePlatform
      }, 
      "name": name
    }
    const headers = new HttpHeaders().set('email', this.currentUserId).set('Content-Type','application/json')            
    return this.http.post<any>(this.rootURL + 'item', body, {headers} ).toPromise()
  }


  postAcceptSwap(counterPartyItemID, proposerItemID) {
    return this.http.post<any>(this.rootURL + 'swap/accept?counterPartyItemID=' + counterPartyItemID + '&proposerItemID=' + proposerItemID, {
      
    }).toPromise()
  }

  postRejecttSwap(counterPartyItemID, proposerItemID) {
    return this.http.post<any>(this.rootURL + 'swap/reject?counterPartyItemID=' + counterPartyItemID + '&proposerItemID=' + proposerItemID, {
      
    }).toPromise()
  }

  getUserInfo(userId) {
    console.log('get user');
    return this.http.get<any>(this.rootURL + 'user', {
      params: {
        email: userId
           }
    }).toPromise()
  }

  getcurrentUserInfo() {
    console.log('get user');
    return this.http.get<any>(this.rootURL + 'user', {
      params: {
        email: this.currentUserId
           }
    }).toPromise()
  }

  postConfirmSwap(counterPartyID, counterPartyItemID, proposerItemID) {
    let requestBody = {
      "counterPartyID": counterPartyID,
      "counterPartyItemID": counterPartyItemID,
      "proposerID":  this.currentUserId,
      "proposerItemID": proposerItemID
    }

    return this.http.post<any>(this.rootURL + 'swap/confirmswap', requestBody).toPromise();   
  }

  getItemForSwap(itemId) {
    return this.http.get<any[]>(this.rootURL + 'swap/proposeswap', {
      params: {
        userID : this.currentUserId,
        itemID : itemId
           }
    }).toPromise()
  }

  getAuthentication(loginId, password) {
    console.log('get user');
    
    return this.http.get('http://localhost:8081/GameSwap/user/authenticate?email=' + loginId + '&password=' + password, 
    {responseType: 'text'}).toPromise()
    // this.http.get(this.rootURL + 'user', {
    //   params: {
    //     email: loginId
    //   },
    //   observe: 'response'
    // })
    // .toPromise()
    // .then(response => {
    //   console.log(response);
    // })
    // .catch(console.log);
  }

  getPostalCodes() {
    return this.http.get<any>(this.rootURL + 'user/postalcodes').toPromise();
  }

  updateUser(email, password, firstName, lastName, nickname, postalCode, phoneNumber, type, share) {
    console.log('Update user');

    let requestBody = {
      email,
      firstName,
      lastName,
      location: {
        postalCode
      },
      nickName: nickname,
    }

    if (password) {
      requestBody['password'] = password;
    }

    if (phoneNumber) {
      requestBody['phone'] = {
        number: phoneNumber,
        type,
        share
      }
    }

    console.log(requestBody);

    return this.http.put<any>(this.rootURL + 'user', requestBody).toPromise();
  }

  addItem(name,gameType,platform, media,pieceCount, condition,description) {
    console.log('Add item');
    let requestData = {
      name,
      gameType,
      platform, 
      media,
      pieceCount, 
      condition,
      description
      
    }

    return this.http.post<any>(this.rootURL + 'item', requestData).toPromise();
  }

  getSwapHistory(userId) {
    return this.http.get<any>(this.rootURL + 'swap/swaphistory', {
      params: {
        userID: userId
      }
    }).toPromise();
  }

  updateRating(swapId, userId, rating) {
    return this.http.post<any>(`${this.rootURL}swap/ratingupdate?userID=${userId}&swapID=${swapId}&rating=${rating}`, {}).toPromise(); 
  }

  getUnratedSwaps(userId) {
    return this.http.get<any>(`${this.rootURL}swap/unratedswap`, {
      params: {
        userID: userId
      }
    }).toPromise();
  }

  getUserOwnItem() {
    return this.http.get<any>(`${this.rootURL}item/owned`, {
      params: {
        email: this.currentUserId
      }
    }).toPromise();
  }

  getUserOwnSummary() {
    console.log('get user');
    return this.http.get<any>(this.rootURL + 'item/owned/summary', {
      headers: {
        email: this.currentUserId
      }
    }).toPromise()  
  }

  getSwapDetails(swapId) {
    
    return this.http.get<any>(this.rootURL + '/swap/swapdetails?swapID=' + swapId + '&userID=' + this.currentUserId, {      
    }).toPromise()  
  }
}
