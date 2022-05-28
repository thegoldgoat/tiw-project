import { eventBus } from './components/EventBus'
import { LogoutButton } from './components/LogoutButton'
import { Router } from './components/Router'
import { Toast, ToastMessage } from './components/ToastComponent'
import './css/bootstrap.min.css'

const appElement = document.querySelector<HTMLDivElement>('#app')!
const logoutElement = document.querySelector<HTMLDivElement>('#logoutButton')!

const router = new Router(appElement)
const logoutButton = new LogoutButton(logoutElement)

const toastComponent = new Toast(document.getElementById('toast')!)

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

eventBus.addSubscriber('gotoImage', (imagePk) => {
  router.openImage(imagePk)
})

eventBus.addSubscriber('toast', (toastMessage: ToastMessage) => {
  toastComponent.updateToast(toastMessage)
})

router.mount()
logoutButton.mount()
toastComponent.mount()

document.getElementById('home')!.onclick = (event) => {
  event.preventDefault()
  router.gotoHome()
}
