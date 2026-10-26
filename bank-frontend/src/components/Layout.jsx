import Header from './Header';
import Navbar from './Navbar';
import Sidebar from './Sidebar';
//import Footer from './Footer';

function Layout({ children }) {
  return (
    <div className="app-shell">
      <Header />
      <Navbar />

      <div className="main-wrapper">
        <Sidebar />
        <main className="content-area">{children}</main>
      </div>

      {/* <Footer /> */}
    </div>
  );
}

export default Layout;