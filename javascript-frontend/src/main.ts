import { eventBus } from './components/EventBus'
import { LogoutButton } from './components/LogoutButton'
import { Router } from './components/Router'
import './css/bootstrap.min.css'

const appElement = document.querySelector<HTMLDivElement>('#app')!
const logoutElement = document.querySelector<HTMLDivElement>('#logoutButton')!

const router = new Router(appElement)
const logoutButton = new LogoutButton(logoutElement)

eventBus.addSubscriber('logout', (event) => router.updateAuthStatus(event))
eventBus.addSubscriber('logged', () => {
  logoutButton.isLogged = true
  logoutButton.update()
})

eventBus.addSubscriber('receivedAlbums', () => {
  logoutButton.isLogged = true
  logoutButton.update()
})

eventBus.addSubscriber('logged', (authStatus) => {
  router.updateAuthStatus(authStatus)
})

eventBus.addSubscriber('openAlbum', (albumPk) => {
  router.openAlbum(albumPk)
})

router.mount()
logoutButton.mount()

document.getElementById('home')!.onclick = (event) => {
  event.preventDefault()
  router.gotoHome()
}
