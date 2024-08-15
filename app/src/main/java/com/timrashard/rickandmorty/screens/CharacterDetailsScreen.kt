package com.timrashard.rickandmorty.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timrashard.network.model.KtorClient
import com.timrashard.network.model.domain.Character
import com.timrashard.rickandmorty.components.character.CharacterDetailsNamePlateComponent
import com.timrashard.rickandmorty.components.common.CharacterImage
import com.timrashard.rickandmorty.components.common.DataPoint
import com.timrashard.rickandmorty.components.common.DataPointComponent
import com.timrashard.rickandmorty.components.common.LoadingState
import com.timrashard.rickandmorty.ui.theme.RickAction
import kotlinx.coroutines.delay

@Composable
fun CharacterDetailsScreen(
    ktorClient: KtorClient,
    characterId: Int
) {
    var character by remember { mutableStateOf<Character?>(null) }

    val characterDataPoints : List<DataPoint> by remember {
        derivedStateOf {
            buildList {
                character?.let { data ->
                    add(DataPoint("Last known location", data.location.name))
                    add(DataPoint("Species", data.species))
                    add(DataPoint("Gender", data.gender.displayName))
                    data.type.takeIf { it.isNotEmpty() }?.let { type ->
                        add(DataPoint("Type", type))
                    }
                    add(DataPoint("Origin", data.origin.name))
                    add(DataPoint("Episode count", data.episodeUrls.size.toString()))
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(500)
        character = ktorClient.getCharacter(characterId)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        if (character == null) {
            item { LoadingState() }
            return@LazyColumn
        }

        // Name Plate
        item{
            CharacterDetailsNamePlateComponent(
                name = character!!.name,
                status = character!!.status
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Image
        item {
            CharacterImage(imageUrl = character!!.imageUrl)
        }

        // Data Points
        items(characterDataPoints){
            Spacer(Modifier.height(32.dp))
            DataPointComponent(dataPoint = it)
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        // Button
        item{
            Text(
                text = "View all episodes",
                fontSize = 18.sp,
                color = RickAction,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = RickAction,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        // TODO
                    }
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(64.dp)) }
    }
}