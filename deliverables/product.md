# product

# Team21 - LotSpot!

#### Q1: What are you planning to build?

We are planning to build an android application that finds available public parking locations. There are two parts of the application that we are going to build, one side is the users
end, which will be an android app that displays a map with parking lots. The parking lot will be green, yellow or red indicating how many available parking spots are available.
The other side of the applications is a central server, the server will receive updated information from parking locations and update their local database so the users have 
real time accuracy of how many lots are available.

Concrete example of a common use case: User is on vacation with a rental car, unfamiliar with the area they do not want to waste time driving around searching for a spot,
they open the application and can see all available parking spots near their location.
On the other end, a new parking lot opens and they want to bring in new clients, rather on relying on people to visually see the new lot and post many signs they sign up with
the application and their lot becomes easy to locate for all users.

#### Q2: Who are your target users?

Our target users are drivers that come downtown for events. These people are trying to find parking during high traffic and finding a spot can seriously 
become an issue. Additionaly, events are not exclusive to large public events. A regular downtown friday is busy enough to cause issues for club goers trying to find parking.
Our personas provide better clarity for what kind of demographic we are trying to hit. However, they are all commuters trying to find parking during a busy event day.

Complete persona: [Young mother persona](https://app.xtensio.com/folio/k8jma27e)

Some other quick personas (not created with xtensio): 
- Middle aged man who is coming alone downtown to watch the blue jays game.
- University of Waterloo student that is coming downtown to watch a tiff premier she has been excited for.
- Young proffesional living in missisauga who is coming downtown on friday night to go clubbing. She is the designated driver for her friends who will be joining her.

#### Q3: Why would your users choose your product? What are they using today to solve their problem/need?

Our product incorporates an element not seen by other similar applications/tools. It shows the number of parking lots available. This will not be available for every lot as 
the parking garage themselves must integrate some system to keep track of available lots. However, it will still function just as well as other parking garage
finding applications even if the parking garage does not have the proper techonology. This also allows users to make more informed decisions, with information not readily available by other parking applications.

Some similar products people use today to find parking are [ParkMe Parking](https://itunes.apple.com/ca/app/parkme-parking/id417605484?mt=8), 
which is an app that helps users locate parking garages, and users are also able to reserve spots. Another similar web application,  [Parkopedia](http://en.parkopedia.ca/parking/locations/toronto_on_canada_dpz83dffmxp/?arriving=201702061230&leaving=201702061430), finds parking garages and their prices. Our application will have similar functionality as there will be a map interface with pinned lots, but
it will also show the number of available spots to save users' time and money. They do not have to physically go to a garage just to realize that there are no more vacant lots.

Parking lot owners and cooporates would have a large incentive to attract customers. Our application will advertise their location with an easy to find interface. As opposed to making signs to show drivers their parking garage/lot. 

----

### Highlights

During our team meetings we considered several alternatives, including:

- Parking share application that allowed users to share their parking spot with others
	- We decided not to go this route after speaking to our TA about the idea during tutorial. They mentioned that it was not an original concept, and that we should try to avoid the idea of shared economy as the market is already quite oversaturated. From this idea though we focused our sights on parking garages.

- Application to help those with ALS to navigate their phones with an eye tracker
	- Idea was scrapped because we were not familiar enough with the hardware and tools that would be involved in making the application

- Book Sharing application that keeps track of books users have shared with one another as well as a community aspect that has groups of users sharing books with each other at random
	- We chose not to go with idea because most of the app would be CRUD (Create, Remove, Update, Delete)

Our final decision:
- After our second meeting during the lab we bounced our ideas off the TA, and received some valuable feedbacks. He confirmed the originality of our idea, and mentioned that the scope of our application was proper. He also recommended that we could emulate car lots for the purpose of demoing the application.
- We chose to go with our parking lot idea, since it is practical enough that it solves a problem many of us have experienced: the struggle to find a vacant parking space during peak hours, or when there are events going on (such as a Blue Jay's game or TIFF). The app is highly user-centred. It efficiently saves users' time and allows users to discover newer and better information about parking and even the city in general, which enables users to make more informed decisions with ease.

#### MVP features

Phone Application:

- Maps
	- Google Maps
	- To search addresses and locations
	
- Indicate all parking lots with pins on the map
	- Color coded pins
	- Green: vacant; Yello: busy; Red: full

- Able to select a parking spot to get additional information (symbols used for each attribute)
	- Addresses
	- Rates/costs
	- Spots remaining
	
- Search
	- Satisfy different needs such as accessibility, AC, etc.
	- Using Google Maps

Server Side:

- Parking lot side application
	- Keeps track of the number of available spots
	- Updates the server whenever there is a change
	- Receives information from sensors


Additional Features (done after meeting MVP features):

- Filtering and ranking
	- Filters and/or ranks parking lots by price
	- Filter and/or ranks parking lots by additional information (such as height limit, accessibility ect)

- Parking recommendations
	- Input what hours you wish to park your car and recieve the best fitting parking lots (on compatibility)

- Additional parking lot informaiton
	- Add information to parking locations that can be displayed and searched on
	- Accessibility
	- Height limit
	- Overnight parking
	- Hours of operation


#### Application Mockup:

Main
![alt tag](https://github.com/csc301-winter-2017/project-team-21/blob/master/deliverables/main.jpg)

Mini Info
![alt tag](https://github.com/csc301-winter-2017/project-team-21/blob/master/deliverables/miniInfo.jpg)

Details
![alt tag](https://github.com/csc301-winter-2017/project-team-21/blob/master/deliverables/details.jpg)

Navigation
![alt tag](https://github.com/csc301-winter-2017/project-team-21/blob/master/deliverables/navigation.jpg)

