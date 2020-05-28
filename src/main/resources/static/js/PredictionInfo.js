const e = React.createElement;
class PredictionInfo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: true,
            data: [],
            coin: '',
        };
    }

    // Grabs the code from the URL and returns its value
    getUrlVars = () =>{
        const url_string = window.location.href;
        const url = new URL(url_string);
        return url.searchParams.get("code");
    }
    
//PredictionData lies here

    // defines components within the class
    componentDidMount = () => {
        if(this.state.loading){
            this.setState({
                coin: this.getUrlVars(),
                loading: false
                
            })
            setTimeout(this.componentDidMount, 500);
        }else{
            const coin = this.state.coin;
            fetch("/api/currencies/"+coin)
            .then(response => response.json())
            .then(data => this.setState({data: data}))
            setTimeout(this.componentDidMount, 3000)
        }
    }

    // Calculates the change value
    changeValue = (value,change) => {
        const percent = (change/value) * 100
        const rounded = Math.round(percent * 10000) / 10000
        if(rounded >= 0){
            return (
                <h4 Style="color: #48bf0d; font-weight: 700;">Change: <span className="dispNUM">{rounded}%</span></h4>
            );
        }
        else{
            return (
                <h4 Style="color: #bf0d30; font-weight: 700;">Change: <span className="dispNUM">{rounded}%</span></h4>
            );
        }
    }

    render() {
        return(
            <div>
                <div className="title-pane">
                    <h2 className="page-title">{this.state.data.name}</h2>
                </div>
                <div Style="width:100%; margin-left:0;" className="pane">
                    <h4>Code: {this.state.coin}</h4>
                    <h4>Current Price: <span className="dispNUM">${Math.round(this.state.data.recentPrice * 10000) / 10000}</span></h4>
                    {this.changeValue(this.state.data.recentPrice, this.state.data.recentChange)}
                </div>
            </div>
        )
    }
}
const windowElement = document.getElementById('Coin-Data');
ReactDOM.render(e(PredictionInfo), windowElement);