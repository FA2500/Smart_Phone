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
    <link rel="stylesheet" href="{{ URL::asset('css/styles.css') }}">

    <title>Invoice..!</title>
</head>

<body>
    <div class="container my-5 page" size="A4">
        <div class="p-5">
            <section class="top-content bb d-flex justify-content-between">
                <div class="logo">
                    <img src="{{ URL::asset('images/logoin.png') }}" alt="" class="img-fluid">
                </div>
                <div class="top-left">
                    <div class="invoice">
                        <p>INVOICE</p>
                    </div>
                    <div class="position-relative">
                        <p>Invoice No. <span>XXXX</span></p>
                    </div>
                </div>
            </section>

            <section class="store-user mt5">
                <div class="col-10">
                    <div class="col-7">
                        <br>
                        <h2><span>Kuala Terengganu </span>> <span>Seremban</span></h2>
                        <p class="date_time"> <span>Saturday</span>, <span>April </span><span>30</span>, <span>2022</span>, <span>12:00PM</span></p>
                    </div>
                </div>
            </section>
            <section>
                <div class="trip-details mt5">
                    <p>TRIP DETAILS</p><hr>
                    <div class="row bb pb-3">
                        <div class="col-sm-4 col-md-6 column">
                            <p><span>Adik Beradik Express</span> </p><p class="hint">Bus</p>
                        </div>
                        <div class="col-sm-4 col-md-6 column">
                            <p><span>12:00PM</span> @ <span>Hentian Bas MBKT</span></p><p class="hint">Departure Time</p>
                        </div>
                    </div>
                </div>
            </section>
            <section>
                <div class="col-sm-4 col-md-6 boarding-dropping mt5">
                    <br>
                    <p>BOARDING/DROPPING</p><hr>
                    <div class="col-sm-4 col-md-6 row bb pb-3">
                        <div class="col-sm-4 col-md-6 column">
                            <p><span>Kuala Terengganu</span> </p><p class="hint">Boarding Point</p>
                        </div>
                        <div class="col-sm-4 col-md-6 column">
                            <p><span>Hentian Bas MBKT</span></p><p class="hint">Boarding Address</p>
                        </div>
                        <div class="col-sm-4 col-md-6 column">
                            <p><span>Terminal One Seremban</span> </p><p class="hint">Dropping Point</p>
                        </div>
                        <div class="col-sm-4 col-md-6 column">
                            <p><span>Seremban</span></p><p class="hint">Dropping Address</p>
                        </div>
                    </div>
                </div>
            </section>
            <section>
                <div class="col-sm-4 col-md-6 traveler-details mt5">
                    <br>
                    <p>TRAVELLER DETAILS</p><hr>
                    <div class="row bb pb-3">
                        <div class="column">
                            <p><span>MOHAMAD DANISH FAZWAN BIN MOHD</span> </p><p class="hint">Age 20</p>
                        </div>
                        <div class="column">
                            <p><span>10A</span></p><p class="hint">Seat</p>
                        </div>
                    </div>
                </div>
            </section>
            <div class="col-sm-4 col-md-6 traveler-details mt5">
                <br>
                <p>CONTACT DETAILS</p><hr>
                <div class="row bb pb-3">
                    <div class="column">
                        <p><span>fzwnmd@gmail.com</span> </p><p class="hint">Email</p>
                    </div>
                    <div class="column">
                        <p><span>60184024159</span></p><p class="hint">Mobile</p>
                    </div>
                </div>
            </div>
        </section>
        <section>
            <div class="price">
                <br>
                <p>Total Amount   : MYR<span>53.43</span></p>
            </div>
        </section>
        </div>
    </div>
</body>
    
</footer>

</html>