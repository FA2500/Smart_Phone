<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <!-- Custom Style -->
    <link rel="stylesheet" href="{{ URL::asset('css/style.css') }}">

    <title>Invoice..!</title>
</head>

<body>
    <div class="my-5 page" size="A4">
        <div class="p-5">
            <section class="top-content bb d-flex justify-content-between">
                <div class="logo">
                    <img src="{{ URL::asset('images/logo.png') }}" alt="" style = "width:180px; height:60px;" class="img-fluid">
                </div>
                <div class="top-left">
                    <div class="logo1">
                        <img src="{{ URL::asset('images/logoinvoice.png') }}" alt="" style = "width:100px; height:30px;" class="img-fluid">
                    </div>
                    <div class="position-relative">
                        <p>Invoice No: <span>XXXX</span></p>
                        <p>Invoice Date: <span>XXXX</span></p>
                    </div>
                </div>
            </section>

            <section class="store-user mt-5">
                <div class="col-10">
                    <div class="row bb pb-3">
                        <div class="col-7">
                            <h2p><b>Reserve4U</b></h2>
                            <p class="address"> 777 Brockton Avenue, <br> Abington MA 2351, <br>Vestavia Hills AL </p>
                            <p>reserve4u@gmail.com </p>
                        </div>
                        <div class="col-5">
                            <p><b>Invoice No</b> : RS-532698</p>
                            <p><b>Booking ID</b> : SF3SHRFH</p>
                            <p><b>Invoice Date</b> : 25 Jun 2022</p>
                            <p>Total (RM) : RM1210.00</p>
                        </div>
                    </div>
                    <div class="traveler-details mt5">
                        <br>
                        <p><b>Transaction Confirmed</b></p><hr>
                    </div>
                    <div class="row extra-info pt-3">
                        <div class="col-7">
                            <p>Dear <span>ALI BIN ABU</span> ,</p>
                            <p>Thank you for booking your table with <span>Reserve4U</span></p>
                        </div>
                    </div>
                </div>
            </section>

            <section class="product-area mt-4">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <td>Item Description</td>
                            <td>Rate</td>
                            <td>People</td>
                            <td>Price</td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <div class="media">
                                    <div class="media-body">
                                        <p class="mt-0 title">Table 1</p>
                                        
                                    </div>
                                </div>
                            </td>
                            <td>first class</td>
                            <td>3</td>
                            <td>RM600</td>
                        </tr>
                        <tr>
                            <td>
                                <div class="media">
                                    <div class="media-body">
                                        <p class="mt-0 title">Table 2</p>
                                    </div>
                                </div>
                            </td>
                            <td>first class</td>
                            <td>3</td>
                            <td>RM600</td>
                        </tr>
                    </tbody>
                </table>
            </section>

            <section class="balance-info">
                <div class="row">
                    <div class="col-8">
                        <p class="m-0 font-weight-bold"> Note: </p>
                        <p>Please show this invoice for confirmation</p>
                    </div>
                    <div class="col-4">
                        <table class="table border-0 table-hover">
                            <tr>
                                <td>Sub Total:</td>
                                <td>RM1200</td>
                            </tr>
                            <tr>
                                <td>Tax:</td>
                                <td>RM10</td>
                            </tr>
                            <tfoot>
                                <tr>
                                    <td>Total:</td>
                                    <td>RM1210</td>
                                </tr>
                            </tfoot>
                        </table>

                        <!-- Signature -->
                        <div class="col-12">
                            <img src="signature.png" class="img-fluid" alt="">
                            <p class="text-center m-0"> Director Signature </p>
                        </div>
                    </div>
                </div>
            </section>

            <!-- Cart BG -->
            <img src="cart.jpg" class="img-fluid cart-bg" alt="">

            <footer>
                <hr>
                <p class="m-0 text-center">
                    View THis Invoice Online At - <a href="#!"> invoice/reserve4u/#868 </a>
                </p>
                <div class="social pt-3">
                    <span class="pr-2">
                        <i class="fas fa-mobile-alt"></i>
                        <span>012XXXXXX</span>
                    </span>
                    <span class="pr-2">
                        <i class="fas fa-envelope"></i>
                        <span>reserve4u@gmail.com</span>
                    </span>
                    <span class="pr-2">
                        <i class="fab fa-facebook-f"></i>
                        <span>/Reserve4U</span>
                    </span>
                    <span class="pr-2">
                        <i class="fab fa-youtube"></i>
                        <span>/unknow</span>
                    </span>
                    <span class="pr-2">
                        <i class="fab fa-github"></i>
                        <span>/example</span>
                    </span>
                </div>
            </footer>
        </div>
    </div>










</body></html>