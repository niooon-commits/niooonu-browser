package com.niooon.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.niooon.browser.R
import com.niooon.browser.model.DiscoverArticle
import com.niooon.browser.ui.theme.GoogleBlue
import com.niooon.browser.ui.theme.TextPrimary
import com.niooon.browser.ui.theme.TextSecondary

val DefaultArticles = listOf(
    DiscoverArticle(
        id = "art-1",
        title = "The Most Beautiful Places on Earth",
        category = "Travel",
        timeAgo = "2h ago",
        imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=400&q=80",
        articleUrl = "https://en.wikipedia.org/wiki/List_of_World_Heritage_Sites"
    ),
    DiscoverArticle(
        id = "art-2",
        title = "How AI is Changing the Future",
        category = "Technology",
        timeAgo = "5h ago",
        imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=400&q=80",
        articleUrl = "https://en.wikipedia.org/wiki/Artificial_intelligence"
    ),
    DiscoverArticle(
        id = "art-3",
        title = "A New Era for Space Exploration",
        category = "Science",
        timeAgo = "1d ago",
        imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=400&q=80",
        articleUrl = "https://en.wikipedia.org/wiki/Space_exploration"
    )
)

@Composable
fun DiscoverFeed(
    articles: List<DiscoverArticle> = DefaultArticles,
    onArticleClick: (DiscoverArticle) -> Unit,
    onSeeMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val outerShape = RoundedCornerShape(26.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 10.dp,
                shape = outerShape,
                ambientColor = Color(0x1F0288D1),
                spotColor = Color(0x1A000000)
            )
            .clip(outerShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xB3FFFFFF),
                        Color(0x80FFFFFF)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF),
                        Color(0x66FFFFFF)
                    )
                ),
                shape = outerShape
            )
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: "Discover" + "See more >"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Discover",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.clickable { onSeeMoreClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "See more",
                        color = GoogleBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_right),
                        contentDescription = "See more",
                        tint = GoogleBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Article Cards
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                articles.forEach { article ->
                    ArticleCard(
                        article = article,
                        onClick = { onArticleClick(article) }
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleCard(
    article: DiscoverArticle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = cardShape,
                ambientColor = Color(0x14000000),
                spotColor = Color(0x14000000)
            )
            .clip(cardShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xCCFFFFFF),
                        Color(0x99FFFFFF)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF),
                        Color(0x4DFFFFFF)
                    )
                ),
                shape = cardShape
            )
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail Image
            val imageShape = RoundedCornerShape(12.dp)
            AsyncImage(
                model = article.imageUrl,
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 84.dp, height = 56.dp)
                    .clip(imageShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Title and Metadata
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${article.category} · ${article.timeAgo}",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // Options 3-dot Icon from Material Symbols
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { /* options */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vert),
                    contentDescription = "Options",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
