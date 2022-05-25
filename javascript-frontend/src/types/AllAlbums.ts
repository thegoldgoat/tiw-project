export type Album = {
  ownerUsername: string
  albumPk: number
  title: string
  date: string
  userFk: number
}

export type AllAlbums = {
  userAlbums: Album[]
  otherUserAlbums: Album[]
}
