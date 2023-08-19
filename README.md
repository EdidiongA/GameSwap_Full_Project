# GameSwap_Full_Project
Phases 1, 2 and 3 of the Database application created with Angular/Node.js (frontend), Java and MySQL (backend)

# Description of GameSwap

GameSwap is a bartering website for trading board games, video game systems, and other home entertainment gaming items. 

In GameSwap, users self-register and enter their following details: email, password, first name, last name, nickname (for display purposes), city, state, postal code and Phone number (optional).

An email can only be registered once in the system. Nicknames do not have this requirement. A phone number can only be registered to a single user. The system will not require “forgot password” or “password reset” functionality.

The GameSwap system also tracks items that will be swapped. For all items, the following details are entered: 
1. The title/name of the item (“name” and “title” may be used interchangeably to describe this attribute)
2. The item’s game type (one of the following, along with the specific details listed for that type):
	a. Board game
	b. Card game
	c. Video game
		i. Platform: This is limited to the choices of Nintendo, PlayStation, or Xbox. This list of values are updatable in the database
		ii. Media: limited to the choices of optical disc, game card, or cartridge
	d. Computer game
		i. Platform: limited to the choices of Linux, macOS, or Windows
	e. Jigsaw puzzle
		i. Piece count
3. Condition: limited to Mint, Like New, Lightly Used, Moderately Used, Heavily Used, or Damaged/Missing parts
4. User-entered description (optional)

When an item is listed, it is assigned an item number by the system, which is an ordinal based on when the item was input (for example, the first item in the system would be assigned item number 1, the second assigned 2, and so on).

Users that have listed items will be able to swap items with each other. Items associated with a pending swap (a proposed swap not yet accepted or rejected) are not available for swapping, as are items associated with completed swaps. For obvious reasons, a user cannot swap items with himself/herself, and a user with no listed items may browse items but cannot swap. For clarity and convenience, the user proposing a swap is called the “proposer” and the user receiving the proposed swap is called the “counterparty”. A proposer will request a swap with a proposed item for one of the counterparty’s items (called the “desired item”). The date the proposal is made will be stored. The counterparty will then accept or reject the swap, with the date of acceptance or rejection also stored. If a swap is rejected, that specific item-for-item swap cannot be proposed again. If a swap is accepted, the counterparty will see the contact information for the proposer, so that they may communicate and determine how to physically swap items. To mark the swap as completed, after swapping items, both users must rate each other, on a scale of 0-5. Items that have been swapped cannot be swapped again, however, a user can enter the item into the system as a new item listing (which may have different/new information, such as an updated condition or description) for another swap.

## Functionality

### Login

When initially accessing the GameSwap application, all users are directed to the login form.

Users login with an email or phone number, and a password. A “register” link is be provided on the login form. If the email or phone number for the user is not registered, or the username/phone number is registered, but the password is incorrect, an appropriate error message is presented to the user.

### Main menu

After logging in or registering, all users are taken to the main menu. On the main menu, there is a “welcome” message at the top of the menu that includes the user’s first and last names. Several statistics will also be displayed: the number of proposed swaps the user has received, which, if greater than zero, links to the accept/reject swap form (if any swaps are more than five days old, or the user has more than five unaccepted swaps, this number is displayed in bold and in red); the number of unrated swaps the user has not yet rated, which, if greater than zero, links to the swap rating page (if greater than 2, this number is displayed in bold and red); the user’s current rating average, rounded to hundredths (or “None” if no ratings have been made for them).

From the main menu, appropriate links to the following functions are be provided: Listing an item; showing a list of the user’s items; searching for items; swap history; and updating the user’s information

### Listing an item

This page is used to collect the information needed for listing an item. If a user has more than two unrated swaps, or more than five unaccepted swaps, they cannot list a new item and an appropriate message will be displayed.

### My items

This form displays any items listed by the user that are available for swapping. The top of the form shows the count of items for each game type, along with the total number of items. The listing includes item number, game type, item name/title, the condition, and the first 100 characters of the description, sorted by ascending order of item number. A detail button or link is provided which allows loading item’s specific details.

### Searching for items

This form will allow the user to search for items by various criteria. The following search options are provided:
1. By keyword (matching any string in either the name/title or description)
2. Within the user’s postal code
3. Within X miles of the user, where X is a user-entered whole number, utilizing the coordinates provided with the postal codes
4. Within a specific postal code entered by the user

Only items available for swapping are included in the search results. If the search yields no results, the message “Sorry, no results found!” is displayed and the user is returned to the search form.

The Search results form’s title and header indicate what type of search has been executed. The search detail shows the item number, game type, item name/title, the condition, the first 100 characters of the description, and the distance from the user (rounded to tenths), sorted by distance and ascending item number. If a keyword search is being performed, then the field(s) that matched the keyword are highlighted with a blue background in the search results. A detail button or link is provided which allows loading item’s specific details.

### View Item 

This form can be accessed from many other forms and displays different elements depending on the context. All details for the item must are shown: item number, title/name, any type-specific attributes, and the description. If no description exists, then the field is not be displayed.

### Proposing a swap 

This form allows a user to select which of their own items they would like to propose to the counterparty for the desired item. If a user has more than 2 unrated swaps, they cannot propose a swap and are not be able to reach this form. If the counterparty is >= 100.0 miles from the user, a warning message containing that distance is shown at the top of the form. The desired item’s title is displayed. An appropriate mechanism for choosing the proposed item is provided, namely a listing with a selector, a list box, a dropdown list, etc. The list for choosing the proposed item shows item number, game type, title, and condition, ordered by item number, and only display items that are available for swapping. Once the proposed item is chosen, the user clicks the Confirm button to confirm the proposal.

### Accept/reject swaps 

This form lists proposed swaps where the user is the counterparty so that they may accept or reject them. For each proposal, the date proposed, desired item’s title, proposer’s nickname, their rating (rounded to hundredths), distance from user (rounded to tenths), and proposed item title, ordered by proposal date are all shown. Both item fields link to the item’s detail page, and an accept/reject button confirms accepting or rejecting the swap.

If the swap is accepted, a dialog is displayed with the proposer’s email, first name, and phone number/type, if available and if sharing option is set. 

If the swap is rejected, a new swap for the proposed item and desired item cannot be proposed again. Accepting a swap removes it from the listing, and if no more swaps need to be accepted or rejected, the user is returned to the main menu.

### Rate swaps 

This form is used to rate the other user in the swap after the swappers have traded items. For any unrated swaps, this form shows the swap acceptance date, the user’s role in the proposal (proposer or counterparty), proposed item title, desired item title, other user’s nickname, and rating choice (0 – 5), ordered by descending order of acceptance date.

### Swap history 

This form displays to the user their history of all swaps. At the top of the form is a summary, showing the logged in user’s total swaps proposed, total received, subtotals for accepted and rejected, and % rejected, rounded to tenths.

Next, a listing of all swaps is displayed, which includes swap proposed date, swap acceptance/rejection date, swap status (accepted or rejected), user’s role in proposal (proposer or counterparty), proposed item title, desired item title, other user’s nickname, and for accepted swaps, the rating that the user gave to the other user, sorted by descending order of acceptance/rejection date and ascending order of swap proposed date. A detail link helps to load the swap details form for a listed swap.

### Swap details

This form shows all the details of the selected swap (proposed date, accepted/rejected date, swap status, swap type) and the rating that the user gave to the other user. It also displays the other user’s nickname, distance, and if an accepted swap, their contact details (first name, email, and phone number/type if available and sharing option is set), and details for both items.
