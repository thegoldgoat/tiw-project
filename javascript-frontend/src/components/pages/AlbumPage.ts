import { AllAlbums } from '../../types/AllAlbums'
import { AlbumList } from '../AlbumList'
import { Page } from './Page'

export class AlbumPage extends Page {
  allAlbums!: AllAlbums

  userAlbumList!: AlbumList
  otherUsersAlbumList!: AlbumList

  protected mounted(): void {
    this.mountElement.innerHTML = `
    <div class="page-header" style="margin-top: 4em;">
        <h3>Your Albums:</h3>
    </div>

    <div id="userAlbums"> </div>
    
    <div class="page-header" style="margin-top: 4em;">
        <h3>Other Users Albums:</h3>
    </div>

    <div id="otherUsersAlbums"> </div>`

    this.userAlbumList = new AlbumList(
      document.getElementById('userAlbums')!,
      this.allAlbums.userAlbums,
      true
    )
    this.otherUsersAlbumList = new AlbumList(
      document.getElementById('otherUsersAlbums')!,
      this.allAlbums.otherUserAlbums,
      false
    )

    this.userAlbumList.mount()
    this.otherUsersAlbumList.mount()
  }

  protected showState(): void {
    this.mounted()
  }
}
