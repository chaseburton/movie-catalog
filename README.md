# Movie Catalog

## Project Layout:

The general layout of the project is as follows:

**com.shef.movieapp**
- **data (package)**
    - MovieDetail
    - MovieResponse
    - MovieViewModel
- **RecyclerView (package)**
    - CategoryAdapter
    - ImageCardAdapter
- **services (package)**
    - MovieRepository
    - MovieService
- BaseActivity
- CategoryData
- DiscoverActivity
- FavouritesActivity
- SharedPreferencesHelper
- MainActivity
- MovieDetailsActivity

## Project Description:

The **services** package handles the API requests and passes the information to the **data** package. The MovieRepository calls MovieService in order to maintain separation of concerns.

Inside the **data** package, MovieDetails are serialized in order to maintain the same consistency with the API. Otherwise, text like poster_path becomes posterPath.

MovieResponse is also serialized for the same reason. It handles the "results" of each json response.

Gson is used in the MovieViewModel to handle requests because it has the most documentation available. Inside that class fetchMovies() handles the first page of movies to display on the home page. fetchMovieDetail takes the id and returns the movieDetailResponse values.

The RecyclerView uses a CategoryAdapter and ImageCardAdapter to display the view of category and image items from the context.

BaseActivity handles the behavior that should be consistent across classes, most notably informing the user if they are disconnected from the internet which was a core requirement.

CategoryData is a simple data class used by the CategoryAdapter.

The MainActivity utilizes the MovieViewModel to fetch the initial movies to display. It sorts them using a genreMap corresponding to the TMDB genre listing numeration.

All the activities (MainActivity, DiscoveryActivity, and FavouritesActivity) have a navigation bar that can be used to reload the current page or move to another.

The MainActivity also includes an intuitive feature to refresh the movies by flicking down which reloads the movies. On each reload, the movies are randomized within their categories to demonstrate that an action has taken place. Movies with multiple IDs are found in each category since that is expected behavior and the API is limited so some categories would only contain a single movie.

If a user selects a movie from the MainActivity they are taken the a second MovieDetailsActivity page which displays the description, rating, and release date. They can leave that page by swiping left to return to the home page.

The DiscoverActivity acts like Tinder where the user can swipe left to see a new movie which turns the card red to demonstrate it is being removed, and the reverse for green where they are added to the Favourites page via storing the movieId in preferences.

In the FavouritesActivity the user can see movies they have favourited and view them using the same MovieDetailsActivity.

The remove button removes the first movie from the list.

Overall the application fulfills the core requirements and offers some unique functionality.
