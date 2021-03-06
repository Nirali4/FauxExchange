const e = React.createElement;
class Dashboard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            user: [],
            accountValues: '',
            holdings: [],
            orders: [],
            session:'',
            rand: 1,
            friends: [],
            leaderboard: []
        };
    }
    
    //Fetchs from rest API
    componentDidMount = () => {
        const num = Math.floor(Math.random() *4)+1
        //fetch session
        fetch("/api/session")
        .then(response => response.json())
        .then(data => this.setState({session: data}))

        console.log("Getting User Data");
        fetch('/api/session/info')
        .then(response => response.json())
        .then(data => this.setState({user: data}))

        //fetch accountValues
        fetch("/api/session/value")
        .then(response => response.json())
        .then(data => this.setState({accountValues: data}))

        //fetch holdings
        fetch("/api/session/crypto")
        .then(response => response.json())
        .then(data => this.setState({holdings: data}))

        //fetch orders
        fetch("/api/session/orders")
        .then(response => response.json())
        .then(data => this.setState({orders: data}))

        //fetch friends
        fetch("/api/session/friends")
        .then(response => response.json())
        .then(data => this.setState({friends: data}))

        //fetch leaderboards
        fetch("/api/user/leaderboard")
        .then(response => response.json())
        .then(data => this.setState({leaderboard: data}))

        //refresh
        setTimeout(this.componentDidMount, 3000);
    }

    // Clicks through to the currency page
    currencyClick = (event,code) => {
        event.preventDefault();
        console.log("Clicked");
        console.log(code);
        window.location.href = "/currency?code="+code;
    }

    // Displays user details on the dash
    userprofile(){
        const num = this.state.rand;
        return(
            <div className="pane pain-split-two profile">
                <div Style="display:flex; justify-content: center;">
                    <img className='avatar' src={'images/placeholder/person_'+num+'.jpg'}/> 
                </div>
                <span>Name: {this.state.user.fName + " " + this.state.user.lName}</span>
                <span>Rank</span>
                <span>Cash: <span className="dispNUM">${Math.round(this.state.user.cash * 10000) / 10000}</span></span>
            </div>
        );
    }

    // Applies a css style to Profit/Loss depending on the value
    profitloss = (value) => {
        const rounded = Math.round(value * 10000) / 10000
        if(rounded >= 0){
            return (
                <li className="stats-list-positive">
                    ${Math.round(this.state.accountValues.standings * 10000) / 10000} <span className="stats-list-label">Profit/Loss</span>
                </li>
            );
        }
        else{
            return (
                <li className="stats-list-negative">
                    ${Math.round(this.state.accountValues.standings * 10000) / 10000} <span className="stats-list-label">Profit/Loss</span>
                </li>
            );
        }
    }


    // Displays the account value on the dash
    accountvalue(){
        return(
            <div className="pane pain-split-two account-value">
                <h5>Account Value</h5>
                <ul className="stats-list">
                    <li>
                        ${Math.round(this.state.accountValues.value * 10000) / 10000} <span className="stats-list-label">Account Value</span>
                    </li>
                    {this.profitloss(this.state.accountValues.standings)}
                    {/* <li className="stats-list-positive">
                        ${Math.round(this.state.accountValues.standings * 10000) / 10000} <span className="stats-list-label">Profit/Loss</span>
                    </li> */}
                </ul>
            </div>
        );
    }

    // Displays the invested crypto that the user has bought into 
    holdings(){
        return(
            <div className="pane pain-split-two">
                <h5><i class="fas fa-wallet"> </i>Invested Crypto</h5>
                <table>
                    <tbody>
                        <tr>
                            <th className="text-center"></th>
                            <th className="text-center">Qty</th>
                            <th className="text-center">Value</th>
                        </tr>
                        {this.state.holdings.map(holding => (
                            <tr>
                                <td>
                                    <tr Style="padding:.3em; background:none;">
                                        <td Style="padding:.3em; text-align: left; font-weight:700;" className="text-center">{holding.code}</td>
                                    </tr>
                                    <tr Style="padding:.3em; background:none;">
                                        <td Style="padding:.3em; text-align: left;" className="text-center dispNUM">${Math.round(holding.price * 10000) / 10000}</td>
                                    </tr>
                                </td>
                                <td className="text-center dispNUM">{Math.round(holding.qty * 10000) / 10000}</td>
                                <td className="text-center dispNUM">${Math.round(holding.value * 10000) / 10000}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        );
    }

    // Displays the current orders that the user has placed
    orders(){
        return(
            <div className="pane pain-split-two">
                <h5><i class="fas fa-file-alt"></i> Current orders</h5>
                <table>
                    <tbody>
                        <tr>
                            <th className="text-center"></th>
                            <th className="text-center">Qty</th>
                            <th className="text-center">Value</th>
                            <th className="text-center">Buy/Sell</th>
                        </tr>
                        {this.state.orders.map(order => (
                            <tr key={order.orderId} onClick={(e) =>{this.currencyClick(e,coin.code)}}>
                                <td>
                                    <tr Style="padding:.3em; background:none;">
                                        <td Style="padding:.3em; text-align: left; font-weight:700;" className="text-center">{order.code}</td>
                                    </tr>
                                    <tr Style="padding:.3em; background:none;">
                                        <td Style="padding:.3em; text-align: left;" className="text-center dispNUM">{Math.round(order.price * 10000) / 10000}</td>
                                    </tr>
                                </td>
                                <td className="text-center">{order.qty}</td>
                                <td className="text-center dispNUM">${Math.round((order.price * order.qty) * 10000) / 10000}</td>
                                <td className="text-center">{order.type}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        );
    }

    // Displays the leaderboard to the dash
    leader(){
        return(
            <div className="pane pain-split-two">
                <h5><i class="fas fa-crown"></i> Leader Board</h5>
                <table class="hover">
                    <tbody>
                        <tr>
                            <th class="text-center">Rank</th>
                            <th class="text-center">User</th>
                            <th class="text-center">Value</th>
                        </tr>
                        {this.state.leaderboard.map(user => (
                            <tr>
                                <td className="text-center">{user.rank}</td>
                                <td className="text-center">{user.name}</td>
                                <td className="text-center dispNUM">${Math.round(user.value * 10000) / 10000}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>            
        );
    }

    //  Sends email entered into add friends to the rest API
    friendHandleSubmit = (event) => {
        event.preventDefault();
        const data = new FormData(event.target);
        console.log(stringifyFormData(data));
        fetch('/api/session/addfriends', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: stringifyFormData(data),
        });
        console.log(stringifyFormData(data));
    }

    // Displays the list of friends to the user with a search form
    friends(){
        return(
            <div className="pane pain-split-two">
                <h5><i class="fas fa-user-friends"></i> Friends</h5>
                <form className="friends-search" onSubmit = {this.friendHandleSubmit}>
                    <input className="seach-input" type="text" id="email" name="email"/>
                    <input type="Submit" className="button" value="Add"/>
                </form>
                <table>
                    <tbody>
                        <tr>
                            <th className="text-center">Username</th>
                            <th className="text-center">Value</th>
                        </tr>
                        {this.state.friends.map(friend => (
                            <tr>
                                <td className="text-center">{friend.name}</td>
                                <td className="text-center dispNUM">${Math.round(friend.value * 10000) / 10000}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        );
    }

    // Renders all elements
    render() {
        return(
            <div className="content-split-two">
                {this.userprofile()}
                {this.accountvalue()}
                {this.holdings()}
                {this.orders()}
                {this.leader()}
                {this.friends()}
            </div>
        );
    }
}
const windowElement = document.getElementById('Dash-Content');
ReactDOM.render(e(Dashboard), windowElement);

function stringifyFormData(fd) {
    const data = {};
    for (let key of fd.keys()) {
        data[key] = fd.get(key);
    }
    return JSON.stringify(data, null, 2);
}