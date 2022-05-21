import { LogoutButton } from './components/LogoutButton'
import { Router } from './components/Router'
import './css/bootstrap.min.css'

const appElement = document.querySelector<HTMLDivElement>('#app')!
const logoutElement = document.querySelector<HTMLDivElement>('#logoutButton')!

const router = new Router(appElement)
const logoutButton = new LogoutButton(logoutElement)

logoutButton.addSubscriber('logout', router.updateAuthStatus)

router.mount()
logoutButton.mount()
