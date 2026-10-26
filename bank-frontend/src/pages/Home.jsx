import { useEffect } from "react";
import img1 from "../assets/bank1.jpg";
import img2 from "../assets/bank2.jpg";
import img3 from "../assets/bank3.jpeg";
import { Carousel } from "bootstrap";

function Home() {
  useEffect(() => {
    const carouselElement = document.querySelector("#bankCarousel");

    if (carouselElement) {
      new Carousel(carouselElement, {
        interval: 2000,
        ride: "carousel",
        wrap: true,
        pause: false,
      });
    }
  }, []);

  return (
    <div className="page-card">
      <h1 className="mb-3 text-center fw-bold" style={{ color: "#4da3ff" }}>
        Welcome to the FDM Group
      </h1>

      {/* <p className="mb-4">
        Use the sidebar to manage customers and accounts.
      </p> */}

      <div
        id="bankCarousel"
        className="carousel slide"
      >
        <div className="carousel-indicators">
          <button
            type="button"
            data-bs-target="#bankCarousel"
            data-bs-slide-to="0"
            className="active"
            aria-current="true"
            aria-label="Slide 1"
          ></button>

          <button
            type="button"
            data-bs-target="#bankCarousel"
            data-bs-slide-to="1"
            aria-label="Slide 2"
          ></button>

          <button
            type="button"
            data-bs-target="#bankCarousel"
            data-bs-slide-to="2"
            aria-label="Slide 3"
          ></button>
        </div>

        <div className="carousel-inner rounded overflow-hidden">
          <div className="carousel-item active">
            <img
              src={img1}
              className="d-block w-100 carousel-img"
              alt="bank1"
            />
            <div className="carousel-caption d-none d-md-block">
              <h5>Customer Management</h5>
              <p>Manage customers easily.</p>
            </div>
          </div>

          <div className="carousel-item">
            <img
              src={img2}
              className="d-block w-100 carousel-img"
              alt="bank2"
            />
            <div className="carousel-caption d-none d-md-block">
              <h5>Account Management</h5>
              <p>Create savings and checking accounts.</p>
            </div>
          </div>

          <div className="carousel-item">
            <img
              src={img3}
              className="d-block w-100 carousel-img"
              alt="bank3"
            />
            <div className="carousel-caption d-none d-md-block">
              <h5>Modern Banking UI</h5>
              <p>Responsive and easy to use.</p>
            </div>
          </div>
        </div>

        <button
          className="carousel-control-prev"
          type="button"
          data-bs-target="#bankCarousel"
          data-bs-slide="prev"
        >
          <span className="carousel-control-prev-icon" aria-hidden="true"></span>
          <span className="visually-hidden">Previous</span>
        </button>

        <button
          className="carousel-control-next"
          type="button"
          data-bs-target="#bankCarousel"
          data-bs-slide="next"
        >
          <span className="carousel-control-next-icon" aria-hidden="true"></span>
          <span className="visually-hidden">Next</span>
        </button>
      </div>
    </div>
  );
}

export default Home;